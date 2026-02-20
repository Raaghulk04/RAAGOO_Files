public class JoinsCounterQ extends Event {
  private Counter counter;
  private Customer customer;

  public JoinsCounterQ(Counter counter, Customer customer, double time) {
    super(time);
    this.counter = counter;
    this.customer = customer;
  }

  @Override
  public Event[] simulate() {
    this.counter.assignCust(this.customer);
    return new Event[0];
  }

  @Override
  public String toString() {
    StringBuilder joinsqueue = new StringBuilder();
    String time = String.format("%.3f", this.getTime());

    joinsqueue.append(time).append(": ");
    joinsqueue.append(customer.toString()).append(" joins counter queue (at ");
    joinsqueue.append(counter.toString()).append(")");

    return joinsqueue.toString();
  }
}
