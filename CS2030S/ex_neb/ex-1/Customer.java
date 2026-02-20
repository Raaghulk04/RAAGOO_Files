public class Customer {
  private static int numSofar = 0;

  private int id;
  private double arrivalTime;
  private double serviceTime;

  public Customer(double arrivalTime, double serviceTime) {
    this.id = numSofar++;
    this.arrivalTime = arrivalTime;
    this.serviceTime = serviceTime;
  }

  public int getID() {
    return this.id;
  }

  public double getArrival() {
    return this.arrivalTime;
  }

  public double getService() {
    return this.serviceTime;
  }
}
