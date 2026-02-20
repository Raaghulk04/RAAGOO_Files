import java.util.Random;

/**
 * CS2030S Exercise 0: RandomPoint.java
 * Semester 2, 2025/26
 *
 * <p>The Point class encapsulates a point on a 2D plane.
 *
 * @author XXX
 */
class RandomPoint extends Point {
  private static Random rng = new Random(1);

  public RandomPoint(double minX, double maxX, double minY, double maxY) {
    super(minX + (maxX - minX) * rng.nextDouble(), minY + (maxY - minY) * rng.nextDouble());
  }

  public static void setSeed(int seed) {
    RandomPoint.rng = new Random(seed);
  }
}
