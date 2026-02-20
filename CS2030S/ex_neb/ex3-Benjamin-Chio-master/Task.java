public class Task {
  private String task;
  private double serviceTime;
  private int money;

  public Task(double serviceTime, int task, int money) {
    this.serviceTime = serviceTime;
    this.money = money;
    this.task = task == 0 ? "deposit" : task == 1 ? "withdrawal" : null;
  }

  public double getServiceTime() {
    return this.serviceTime;
  }

  public int charge() {
    return this.task.compareTo("deposit") == 0
        ? this.money
        : this.task.compareTo("withdrawal") == 0 ? -1 * this.money : 0;
  }

  @Override
  public String toString() {
    return task + " of $" + money;
  }
}
