public class ServiceEnd extends Event {
  private Counter counter;

  public ServiceEnd(Counter counter, double time) {
    super(time);
    this.counter = counter;
  }

  @Override
  public Event[] simulate() {
    Departure depart = new Departure(this.counter.departCust(), this.getTime());
    return new Event[] {depart};
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format(
            ": Customer %d service ends (at Counter %d)",
            this.counter.getCustomer().getID(), this.counter.getID());
  }
}
