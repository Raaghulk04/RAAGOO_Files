public class Departure extends Event {
  private Customer customer;

  public Departure(Customer customer, double time) {
    super(time);
    this.customer = customer;
  }

  @Override
  public Event[] simulate() {
    return new Event[0];
  }

  @Override
  public String toString() {
    return super.toString() + String.format(": Customer %d departs", this.customer.getID());
  }
}
