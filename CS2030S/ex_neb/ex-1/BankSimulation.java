import java.util.Scanner;

/**
 * This class implements a bank simulation.
 *
 * @author Wei Tsang
 * @version CS2030S AY25/26 Semester 2
 */
class BankSimulation extends Simulation {
  /**
   * The list of customer arrival events to populate
   * the simulation with.
   */
  private Event[] initEvents;

  /**
   * Constructor for a bank simulation.
   *
   * @param sc A scanner to read the parameters from.  The first
   *           integer scanned is the number of customers; followed
   *           by the number of service counters.  Next is a
   *           sequence of (arrival time, service time) pair, each
   *           pair represents a customer.
   */
  public BankSimulation(Scanner sc) {
    int n = sc.nextInt(); // num of customers
    int k = sc.nextInt(); // num of counters
    Bank bank = new Bank(k);
    Event[] initEvents = new Event[n];
    for (int i = 0; i < n; i++) {
      double arrivalTime = sc.nextDouble();
      double serviceTime = sc.nextDouble();
      Customer customer = new Customer(arrivalTime, serviceTime);
      initEvents[i] = new Arrival(bank, customer);
    }
    this.initEvents = initEvents;
  }

  /**
   * Retrieve an array of events to populate the
   * simulator with.
   *
   * @return An array of events for the simulator.
   */
  @Override
  public Event[] getInitialEvents() {
    return this.initEvents;
  }
}
