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
    StringBuilder servicebegin = new StringBuilder();
    String customer = counter.getCustomer().toString();
    String task = this.counter.getCustomer().getTask().toString();
    String time = String.format("%.3f", this.getTime());

    servicebegin.append(time).append(": ");
    servicebegin.append(customer).append(" ");
    servicebegin.append(task).append(" (at ");
    servicebegin.append(this.counter.toString());
    servicebegin.append(") begins");

    return servicebegin.toString();
  }
}
