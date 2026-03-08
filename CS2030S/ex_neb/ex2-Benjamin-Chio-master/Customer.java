public class Customer {
  private static int numSofar = 0;

  private int id;
  private double arrivalTime;
  private double serviceTime;
  private int task;

  public Customer(double arrivalTime, double serviceTime, int task) {
    this.id = numSofar++;
    this.arrivalTime = arrivalTime;
    this.serviceTime = serviceTime;
    this.task = task;
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

  public String getTask() {
    return this.task == 0 ? "deposit" : "withdrawal";
  }

  @Override
  public String toString() {
    return "C" + this.id;
  }
}
