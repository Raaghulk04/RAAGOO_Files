public class Arrival extends Event {
  private Bank bank;
  private Customer customer;

  public Arrival(Bank bank, Customer customer) {
    super(customer.getArrival());
    this.bank = bank;
    this.customer = customer;
  }

  @Override
  public Event[] simulate() {
    Counter assignedCounter = this.bank.arrival(this.customer);
    if (assignedCounter == null) {
      if (!this.bank.isFullQ()) {
        JoinsQueue queuing = new JoinsQueue(this.bank, this.customer, this.getTime());
        return new Event[] {queuing};
      } else {
        Departure depart = new Departure(this.customer, this.getTime());
        return new Event[] {depart};
      }
    } else {
      ServiceBegin service = new ServiceBegin(this.bank, assignedCounter, this.getTime());
      return new Event[] {service};
    }
  }

  @Override
  public String toString() {
    String qString = this.bank.printQ();
    String tString = Double.toString(this.customer.getArrival());
    return tString + "00: " + customer.toString() + " arrives " + qString;
  }
}
