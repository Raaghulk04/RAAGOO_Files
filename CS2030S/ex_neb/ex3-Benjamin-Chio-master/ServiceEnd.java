public class ServiceEnd extends Event {
  private Bank bank;
  private Counter counter;
  private String status;

  public ServiceEnd(Counter counter, double time, Bank bank) {
    super(time);
    this.bank = bank;
    this.counter = counter;
    this.status = counter.service() ? "success" : "fail";
  }

  @Override
  public Event[] simulate() {
    Departure depart = new Departure(this.counter.departCust(), this.getTime());
    if (this.counter.isEmpty()) {
      Customer c1 = this.bank.deQ();
      if (c1 != null) {
        this.counter.assignCust(c1);
        ServiceBegin service = new ServiceBegin(this.bank, this.counter, this.getTime());
        return new Event[] {depart, service};
      } else {
        return new Event[] {depart};
      }
    } else {
      this.counter.deQ();
      ServiceBegin service = new ServiceBegin(this.bank, this.counter, this.getTime());
      Customer c2 = this.bank.deQ();
      if (c2 == null) {
        return new Event[] {depart, service};
      } else {
        JoinsCounterQ queuing = new JoinsCounterQ(this.counter, c2, this.getTime() + 0.01);
        return new Event[] {depart, service, queuing};
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder serviceend = new StringBuilder();
    String time = String.format("%.3f", this.getTime());
    String customer = counter.getCustomer().toString();
    String task = this.counter.getCustomer().getTask().toString();

    serviceend.append(time).append(": ");
    serviceend.append(customer).append(" ");
    serviceend.append(task).append(" (at ");
    serviceend.append(this.counter.toString());
    serviceend.append(") ends: ").append(this.status);

    return serviceend.toString();
  }
}
