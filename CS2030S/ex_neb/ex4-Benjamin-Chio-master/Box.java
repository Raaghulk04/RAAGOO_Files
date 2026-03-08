/**
 * A generic box storing an item.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
class Box<T> {
  private static final Box<?> EMPTY_BOX = new Box<>(null);
  private final T item;

  private Box(T item) {
    this.item = item;
  }

  // factory method
  public static <T> Box<T> of(T t) {
    if (t == null) {
      return null;
    }

    return new Box<>(t);
  }

  // factory method
  public static <T> Box<T> ofNullable(T t) {
    if (t == null) {
      return Box.empty();
    }
    return Box.of(t);
  }

  public static <T> Box<T> empty() {
    // EMPTY_BOX is of type <?>
    @SuppressWarnings("unchecked")
    Box<T> b = (Box<T>) EMPTY_BOX;
    return b;
  }

  public boolean isPresent() {
    return this.item != null;
  }

  public <U> Box<U> map(Transformer<? super T, U> t) {
    if (this.isPresent()) {
      return Box.ofNullable(t.transform(this.item));
    }
    return Box.empty();
  }

  public Box<T> filter(BooleanCondition<T> bc) {
    if (this.isPresent() && bc.test(this.item)) {
      return this;
    }
    return Box.empty();
  }

  @Override
  public boolean equals(Object o) {
    if (item != null && o != null && o instanceof Box<?>) {
      Box<?> b = (Box<?>) o;
      return item.equals(b.item);
    }

    return this == o;
  }

  @Override
  public String toString() {
    String i = item == null ? "" : item.toString();
    return String.format("[%s]", i);
  }
}
