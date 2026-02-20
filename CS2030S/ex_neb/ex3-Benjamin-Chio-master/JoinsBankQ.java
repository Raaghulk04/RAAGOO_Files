public class JoinsBankQ extends Event {
  private Bank bank;
  private Customer customer;

  public JoinsBankQ(Bank bank, Customer customer, double time) {
    super(time);
    this.bank = bank;
    this.customer = customer;
  }

  @Override
  public Event[] simulate() {
    this.bank.joinsQ(this.customer);
    return new Event[0];
  }

  @Override
  public String toString() {
    StringBuilder joinsqueue = new StringBuilder();
    String queue = this.bank.printQ();
    String time = String.format("%.3f", this.getTime());

    joinsqueue.append(time).append(": ");
    joinsqueue.append(customer.toString()).append(" joins bank queue ");
    joinsqueue.append(queue);

    return joinsqueue.toString();
  }
}
