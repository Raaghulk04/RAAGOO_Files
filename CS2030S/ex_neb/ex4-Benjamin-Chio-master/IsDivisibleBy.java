/**
 * A boolean condition with an integer parameter y that can be
 * apply to another integer x.  Returns true if x is divisible
 * by y, false otherwise.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
class IsDivisibleBy implements BooleanCondition<Integer> {
  private int i;

  public IsDivisibleBy(int i) {
    this.i = i;
  }

  @Override
  public boolean test(Integer i) {
    return i % this.i == 0;
  }
}
