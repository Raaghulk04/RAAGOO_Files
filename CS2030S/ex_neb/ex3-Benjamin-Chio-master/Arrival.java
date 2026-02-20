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
        JoinsBankQ queuing = new JoinsBankQ(this.bank, this.customer, this.getTime());
        return new Event[] {queuing};
      } else {
        Departure depart = new Departure(this.customer, this.getTime());
        return new Event[] {depart};
      }
    } else {
      if (assignedCounter.getCustomer() == this.customer) {
        ServiceBegin service = new ServiceBegin(this.bank, assignedCounter, this.getTime());
        return new Event[] {service};
      } else {
        JoinsCounterQ queuing = new JoinsCounterQ(assignedCounter, this.customer, this.getTime());
        return new Event[] {queuing};
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder arrival = new StringBuilder();
    String queue = this.bank.printQ();
    String time = String.format("%.3f", this.customer.getArrival());

    arrival.append(time).append(": ");
    arrival.append(customer.toString()).append(" arrives ");
    arrival.append(queue);

    return arrival.toString();
  }
}
