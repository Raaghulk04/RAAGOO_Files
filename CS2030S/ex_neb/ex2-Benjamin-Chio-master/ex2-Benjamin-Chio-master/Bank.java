public class Bank {
  private Counter[] counters;
  private Queue queue;

  public Bank(int k, int m) {
    this.counters = new Counter[k];
    for (int i = 0; i < k; i++) {
      this.counters[i] = new Counter(i);
    }
    this.queue = new Queue(m);
  }

  public Counter arrival(Customer customer) {
    if (this.queue.isEmpty()) {
      // If empty, check for empty counter
      Counter s = this.availCounter();
      if (s != null) {
        s.assignCust(customer);
        return s;
      }
    }
    return null;
  }

  public boolean isFullQ() {
    return this.queue.isFull();
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

  public Counter availCounter() {
    for (Counter counter : this.counters) {
      if (counter.isAvail()) {
        return counter;
      }
    }
    return null;
  }

  public Counter deQ() {
    Counter s = this.availCounter();
    if (s != null) {
      Customer c = (Customer) this.queue.deq();
      if (c != null) {
        s.assignCust(c);
        return s;
      }
    }
    return null;
  }

  public String printQ() {
    return this.queue.toString();
  }
}
