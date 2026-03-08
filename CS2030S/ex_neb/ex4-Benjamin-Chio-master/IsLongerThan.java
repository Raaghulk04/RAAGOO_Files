/**
 * A boolean condition with parameter x that can be applied to
 * a string.  Returns true if the string is longer than x; false
 * otherwise.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
class IsLongerThan implements BooleanCondition<String> {
  private int i;

  public IsLongerThan(int i) {
    this.i = i;
  }

  @Override
  public boolean test(String s) {
    return s.length() > this.i;
  }
}
