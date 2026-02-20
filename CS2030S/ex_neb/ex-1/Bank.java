public class Bank {
  private Counter[] counters;

  public Bank(int k) {
    this.counters = new Counter[k];
    for (int i = 0; i < k; i++) {
      this.counters[i] = new Counter(i);
    }
  }

  public Counter arrival(Customer customer) {
    for (Counter counter : this.counters) {
      if (counter.isAvail()) {
        counter.assignCust(customer);
        return counter;
      }
    }
    return null;
  }

  public Customer depart(Counter counter) {
    return counter.departCust();
  }
}
