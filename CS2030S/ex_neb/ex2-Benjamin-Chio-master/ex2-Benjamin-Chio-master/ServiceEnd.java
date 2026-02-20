public class ServiceEnd extends Event {
  private Bank bank;
  private Counter counter;

  public ServiceEnd(Counter counter, double time, Bank bank) {
    super(time);
    this.bank = bank;
    this.counter = counter;
  }

  @Override
  public Event[] simulate() {
    Departure depart = new Departure(this.counter.departCust(), this.getTime());

    Counter s = this.bank.deQ();
    if (s == null) {
      return new Event[] {depart};
    } else {
      ServiceBegin service = new ServiceBegin(this.bank, s, this.getTime());
      return new Event[] {depart, service};
    }
  }

  @Override
  public String toString() {
    String tString = Double.toString(this.getTime());
    String cString = counter.getCustomer().toString();
    String task = this.counter.getCustomer().getTask();
    return tString + "00: " + cString + " " + task + " (at " + this.counter.toString() + ") ends";
  }
}
