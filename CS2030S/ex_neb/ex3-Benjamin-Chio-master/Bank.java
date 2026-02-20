public class Bank {
  private Seq<Counter> counters;
  private Queue<Customer> queue;

  public Bank(int k, int m, int l) {
    this.counters = new Seq<Counter>(k);
    for (int i = 0; i < k; i++) {
      this.counters.set(i, new Counter(i, l));
    }
    this.queue = new Queue<Customer>(m);
  }

  public Counter arrival(Customer customer) {
    if (this.queue.isEmpty()) {
      // If empty, check for empty counter
      Counter s = counters.min();
      if (s != null) {
        if (s.isAvail()) {
          s.assignCust(customer);
        }
        return s;
      }
    }
    return null;
  }

  public boolean isFullQ() {
    return this.queue.isFull();
  }

  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  public boolean joinsQ(Customer customer) {
    if (this.queue.isFull()) {
      return false;
    } else {
      this.queue.enq(customer);
      return true;
    }
  }

  public Customer depart(Counter counter) {
    return counter.departCust();
  }

  public Customer deQ() {
    return this.queue.deq();
  }

  public String printQ() {
    return this.queue.toString();
  }
}
