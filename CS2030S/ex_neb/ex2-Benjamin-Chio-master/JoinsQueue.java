public class JoinsQueue extends Event {
  private Bank bank;
  private Customer customer;

  public JoinsQueue(Bank bank, Customer customer, double time) {
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
    String qString = this.bank.printQ();
    String tString = Double.toString(this.getTime());
    return tString + "00: " + customer.toString() + " joins queue " + qString;
  }
}
