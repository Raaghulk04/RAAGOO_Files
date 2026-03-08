public class ServiceBegin extends Event {
  private Bank bank;
  private Counter counter;

  public ServiceBegin(Bank bank, Counter counter, double time) {
    super(time);
    this.bank = bank;
    this.counter = counter;
  }

  @Override
  public Event[] simulate() {
    double end = this.getTime() + this.counter.getCustomer().getService();
    ServiceEnd service = new ServiceEnd(this.counter, end, this.bank);
    return new Event[] {service};
  }

  @Override
  public String toString() {
    String tString = Double.toString(this.getTime());
    String cString = counter.getCustomer().toString();
    String task = this.counter.getCustomer().getTask();
    return tString + "00: " + cString + " " + task + " (at " + this.counter.toString() + ") begins";
  }
}
