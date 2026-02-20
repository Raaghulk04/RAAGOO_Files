public class Counter implements Comparable<Counter> {
  private boolean available;
  private int id;
  private Customer customer;
  private Queue<Customer> queue;
  private int balance = 100;

  public Counter(int id, int l) {
    this.available = true;
    this.id = id;
    this.customer = null;
    this.queue = new Queue<Customer>(l);
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

  public boolean isFullQ() {
    return this.queue.isFull();
  }

  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  public int getlen() {
    return this.queue.length();
  }

  public void assignCust(Customer p) {
    if (this.available && this.queue.isEmpty()) {
      this.available = false;
      this.customer = p;
    } else {
      this.queue.enq(p);
    }
  }

  public Customer departCust() {
    this.available = true;
    Customer customer = this.customer;
    this.customer = null;
    return customer;
  }

  public Customer deQ() {
    if (this.available) {
      this.available = false;
      this.customer = this.queue.deq();
      return this.customer;
    } else {
      return null;
    }
  }

  public String printQ() {
    return this.queue.toString();
  }

  public boolean service() {
    if (this.customer == null) {
      return false;
    } else {
      int charge = this.customer.getTask().charge();
      if ((balance + charge) < 0) {
        return false;
      } else {
        this.balance += charge;
        return true;
      }
    }
  }

  @Override
  public int compareTo(Counter c) {
    if (!this.isAvail() && this.isFullQ()) {
      return Integer.MAX_VALUE;
    } else {
      if (c == null) {
        return Integer.MIN_VALUE;
      } else {
        if (c.isAvail()) {
          return Integer.MAX_VALUE;
        } else if (this.isAvail()) {
          return Integer.MIN_VALUE;
        } else {
          return this.getlen() - c.getlen();
        }
      }
    }
  }

  @Override
  public String toString() {
    return "S" + this.id + " $" + this.balance + " " + this.printQ();
  }
}
