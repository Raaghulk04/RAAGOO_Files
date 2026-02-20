public class Counter {
  private boolean available;
  private int id;
  private Customer customer;

  public Counter(int id) {
    this.available = true;
    this.id = id;
    this.customer = null;
  }

  public boolean isAvail() {
    return this.available;
  }

  public int getID() {
    return this.id;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void assignCust(Customer p) {
    this.available = false;
    this.customer = p;
  }

  public Customer departCust() {
    this.available = true;
    Customer customer = this.customer;
    this.customer = null;
    return customer;
  }
}
