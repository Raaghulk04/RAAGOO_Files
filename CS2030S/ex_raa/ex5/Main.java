import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.HashMap;
import java.util.Map;

class Main {

  private static class Assessment {
    private final String name;
    private final String grade;

    public Assessment(String name, String grade) {
      this.name = name;
      this.grade = grade;
    }

    public String getName() {
      return name;
    }

    public String getGrade() {
      return grade;
    }
  }

  private static class Course {
    private final String name;
    private final Map<String, Assessment> assessments;

    public Course(String name, Assessment... assessments) {
      this.name = name;
      this.assessments = new HashMap<>();
      for (Assessment a : assessments) {
        this.assessments.put(a.getName(), a);
      }
    }

    public String getName() {
      return name;
    }

    public Assessment getAssessment(String name) {
      return assessments.get(name);
    }
  }

  private static class Student {
    private final Map<String, Course> courses;

    public Student(Course... courses) {
      this.courses = new HashMap<>();
      for (Course c : courses) {
        this.courses.put(c.getName(), c);
      }
    }

    public Course getCourse(String name) {
      return courses.get(name);
    }
  }

  /*
  public static String getGrade(
      String studentName, String courseName, String assessmentName, Map<String, Student> db) {

    Student student = db.get(studentName);

    if (student == null) {
      return "No such entry";
    }

    Course course = student.getCourse(courseName);
    if (course == null) {
      return "No such entry";
    }

    Assessment assessment = course.getAssessment(assessmentName);
    if (assessment == null) {
      return "No such entry";
    }

    return assessment.getGrade();
  }
  */
  public static String getGrade(
      String studentName, String courseName, String assessmentName, Map<String, Student> db) {

    Transformer<Student, Maybe<Course>> getCourse =
        new Transformer<>() {
          public Maybe<Course> transform(Student s) {
            return Maybe.of(s.getCourse(courseName));
          }
        };

    Transformer<Course, Maybe<Assessment>> getAssessment =
        new Transformer<>() {
          public Maybe<Assessment> transform(Course c) {
            return Maybe.of(c.getAssessment(assessmentName));
          }
        };

    Transformer<Assessment, Maybe<String>> getGrade =
        new Transformer<>() {
          public Maybe<String> transform(Assessment a) {
            return Maybe.of(a.getGrade());
          }
        };

    return Maybe.of(db.get(studentName))
        .flatMap(getCourse)
        .flatMap(getAssessment)
        .flatMap(getGrade)
        .toString();
  }

  public static void main(String[] args) {
    Map<String, Student> db =
        Map.of(
            "Steve",
                new Student(
                    new Course(
                        "CS2030S",
                        new Assessment("ex1", "A"),
                        new Assessment("ex2", "A-"),
                        new Assessment("ex3", "A+"),
                        new Assessment("ex4", "B"),
                        new Assessment("pe1", "C")),
                    new Course(
                        "CS2040S",
                        new Assessment("ex1", "A"),
                        new Assessment("ex2", "A+"),
                        new Assessment("ex3", "A+"),
                        new Assessment("ex4", "A"),
                        new Assessment("midterm", "A+"))),
            "Tony",
                new Student(
                    new Course(
                        "CS2030S",
                        new Assessment("ex1", "C"),
                        new Assessment("ex2", "C"),
                        new Assessment("ex3", "B-"),
                        new Assessment("ex4", "B+"),
                        new Assessment("pe1", "A"))));

    System.out.println(Main.getGrade("Steve", "CS2030S", "ex1", db));
    System.out.println(Main.getGrade("Steve", "CS2030S", "ex2", db));
    System.out.println(Main.getGrade("Steve", "CS2040S", "ex3", db));
    System.out.println(Main.getGrade("Steve", "CS2040S", "ex4", db));
    System.out.println(Main.getGrade("Tony", "CS2030S", "ex1", db));
    System.out.println(Main.getGrade("Tony", "CS2030S", "midterm", db));
    System.out.println(Main.getGrade("Tony", "CS2040S", "ex4", db));
    System.out.println(Main.getGrade("Bruce", "CS2040S", "ex4", db));
  }
}
