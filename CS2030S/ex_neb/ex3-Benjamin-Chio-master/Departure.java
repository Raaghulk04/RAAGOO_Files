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
    StringBuilder departure = new StringBuilder();
    String time = String.format("%.3f", this.getTime());

    departure.append(time);
    departure.append(": ");
    departure.append(this.customer.toString());
    departure.append(" departs");

    return departure.toString();
  }
}
