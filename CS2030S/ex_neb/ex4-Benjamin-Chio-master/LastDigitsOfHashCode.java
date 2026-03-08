/**
 * A transformer with a parameter k that takes in an object x
 * and outputs the last k digits of the hash value of x.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
class LastDigitsOfHashCode implements Transformer<Object, Integer> {
  private int k;

  public LastDigitsOfHashCode(int k) {
    this.k = 1;
    for (int i = 0; i < k; i++) {
      this.k *= 10;
    }
  }

  @Override
  public Integer transform(Object o) {
    int i = o.hashCode();
    i = i >= 0 ? i : -i;
    return k > i ? i : i % k;
  }
}
