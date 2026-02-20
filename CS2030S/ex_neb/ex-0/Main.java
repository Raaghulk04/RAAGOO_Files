import java.util.Scanner;

/**
 * CS2030S Exercise 0: Estimating Pi with Monte Carlo
 * Semester 2, 2025/26
 *
 * <p>This program takes in two command line arguments: the
 * number of points and a random seed.  It runs the
 * Monte Carlo simulation with the given argument and print
 * out the estimated pi value.
 *
 * @author XXX (00X)
 */
class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int numOfPoints = sc.nextInt();
    int seed = sc.nextInt();

    double pi = estimatePi(numOfPoints, seed);

    System.out.println(pi);
    sc.close();
  }

  public static double estimatePi(long numOfPoints, int seed) {
    Point p = new Point(0.5, 0.5);
    Circle c = new Circle(p, 0.5);
    RandomPoint.setSeed(seed);
    long inCircle = 0;
    for (long i = 0; i < numOfPoints; i++) {
      if (c.contains(new RandomPoint(0, 1, 0, 1))) {
        inCircle += 1;
      }
    }
    return 4.0 * inCircle / numOfPoints;
  }
}
