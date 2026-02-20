/**
 * The Seq<T> class for CS2030S
 *
 * @author XXX
 * @version CS2030S AY25/26 Semester 3
 */
class Seq<T extends Comparable<T>> { // TODO: Change to bounded type parameter
  private T[] seq;

  public Seq(int size) {
    // TODO
    @SuppressWarnings("unchecked")
    T[] seq = (T[]) new Comparable<?>[size];
    this.seq = seq;
  }

  public void set(int index, T item) {
    // TODO
    seq[index] = item;
  }

  public T get(int index) {
    // TODO
    return seq[index];
  }

  public T min() {
    // TODO
    try {
      T min = null;
      for (T item : seq) {
        if (item.compareTo(min) < 0) {
          min = item;
        }
      }
      return min;
    } catch (Exception runtime) {
      T min = seq[0];
      for (T item : seq) {
        if (item.compareTo(min) < 0) {
          min = item;
        }
      }
      return min;
    }
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("[ ");
    for (int i = 0; i < this.seq.length; i++) {
      s.append(i + ":" + this.seq[i]);
      if (i != this.seq.length - 1) {
        s.append(", ");
      }
    }
    return s.append(" ]").toString();
  }
}
