public class ServiceBegin extends Event {
  private Counter counter;

  public ServiceBegin(Counter counter, double time) {
    super(time);
    this.counter = counter;
  }

  @Override
  public Event[] simulate() {
    double end = this.getTime() + this.counter.getCustomer().getService();
    ServiceEnd service = new ServiceEnd(this.counter, end);
    return new Event[] {service};
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format(
            ": Customer %d service begins (at Counter %d)",
            this.counter.getCustomer().getID(), this.counter.getID());
  }
}
