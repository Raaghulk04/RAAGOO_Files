public class Customer {
  private static int numSofar = 0;

  private int id;
  private double arrivalTime;
  private Task task;

  public Customer(double arrivalTime, double serviceTime, int task, int money) {
    this.id = numSofar++;
    this.arrivalTime = arrivalTime;
    this.task = new Task(serviceTime, task, money);
  }

  public int getID() {
    return this.id;
  }

  public double getArrival() {
    return this.arrivalTime;
  }

  public double getService() {
    return this.task.getServiceTime();
  }

  public Task getTask() {
    return this.task;
  }

  @Override
  public String toString() {
    return "C" + this.id;
  }
}
