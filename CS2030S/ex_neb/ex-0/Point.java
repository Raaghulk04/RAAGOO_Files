/**
 * CS2030S Exercise 0: Point.java
 * Semester 2, 2025/26
 *
 * <p>The Point class encapsulates a point on a 2D plane.
 *
 * @author XXX
 */
class Point {
  // fields
  private double x;
  private double y;

  // Constructor
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // Accessors
  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  // Mutators
  public void newX(double x) {
    this.x = x;
  }

  public void newY(double y) {
    this.y = y;
  }

  // Methods
  public double squaredDist(Point p) {
    double x = p.x - this.x;
    double y = p.y - this.y;
    return x * x + y * y;
  }

  // toString
  @Override
  public String toString() {
    return ("(" + x + ", " + y + ")");
  }
}
