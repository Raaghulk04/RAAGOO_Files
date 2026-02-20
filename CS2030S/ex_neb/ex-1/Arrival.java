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
      Departure depart = new Departure(this.customer, this.getTime());
      return new Event[] {depart};
    } else {
      ServiceBegin service = new ServiceBegin(assignedCounter, this.getTime());
      return new Event[] {service};
    }
  }

  @Override
  public String toString() {
    return super.toString() + String.format(": Customer %d arrives", this.customer.getID());
  }
}
