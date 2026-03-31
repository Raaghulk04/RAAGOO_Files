package cs2030s.fp;

/**
 * CS2030S Exercise 5
 * AY25/26 Semester 2
 *
 * @author Raaghul (14C)
 */
public abstract class Maybe<T> {
  private static None nil = new None();

  public static Maybe<Object> none() {
    return Maybe.nil;
  }

  public static <T> Maybe<T> some(T t) {
    return new Some<T>(t);
  }

  @SuppressWarnings("unchecked")
  public static <T> Maybe<T> of(T t) {
    return (t == null) ? (Maybe<T>) Maybe.none() : Maybe.some(t);
  }

  protected abstract T get() throws NoSuchElementException;

  public abstract Maybe<?> filter(BooleanCondition<?> f);

  public abstract <U> Maybe<U> map(Transformer<?, ?> t) throws NullPointerException;

  public abstract <U> Maybe<U> flatMap(Transformer<? super T, ? extends Maybe<? extends U>> t)
      throws NullPointerException;

  public abstract <S> Object orElse(S s);

  public abstract T orElseGet(Producer<? extends T> p);

  public abstract void ifPresent(Consumer<? super T> c);

  @Override
  public abstract boolean equals(Object m);

  private static class None extends Maybe<Object> {

    @Override
    public String toString() {
      return "[]";
    }

    @Override
    public boolean equals(Object n) {
      return n instanceof None;
    }

    @Override
    public Object get() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    @Override
    public Maybe<Object> filter(BooleanCondition<?> f) {
      return this;
    }

    @Override
    public <U> Maybe<U> map(Transformer<?, ?> t) throws NullPointerException {
      @SuppressWarnings("unchecked")
      Maybe<U> res = (Maybe<U>) this;
      return res;
    }

    @Override
    public <U> Maybe<U> flatMap(Transformer<? super Object, ? extends Maybe<? extends U>> t)
        throws NullPointerException {
      return this.map(t);
    }

    @Override
    public <S> Object orElse(S s) {
      return s;
    }

    @Override
    public Object orElseGet(Producer<? extends Object> p) {
      return p.produce();
    }

    @Override
    public void ifPresent(Consumer<Object> c) {}
  }

  private static class Some<T> extends Maybe<T> {
    private T item;

    public Some(T t) {
      this.item = t;
    }

    @Override
    public String toString() {
      return String.format("[%s]", this.item);
    }

    @Override
    public boolean equals(Object s) {
      if (s instanceof Some<?>) {
        Some<?> s1 = (Some<?>) s;
        return (this.item == s1.item);
      }
      return false;
    }

    @Override
    public T get() {
      return this.item;
    }

    @Override
    public Maybe<?> filter(BooleanCondition<?> f) {
      @SuppressWarnings("unchecked")
      BooleanCondition<T> casted = (BooleanCondition<T>) f;
      return this.item == null ? Maybe.some(null) : casted.test(this.item) ? this : Maybe.none();
    }

    @Override
    public <U> Maybe<U> map(Transformer<?, ?> t) throws NullPointerException {
      @SuppressWarnings("unchecked")
      Transformer<T, U> casted = (Transformer<T, U>) t;
      return Maybe.some(casted.transform(this.item));
    }

    @Override
    public <U> Maybe<U> flatMap(Transformer<? super T, ? extends Maybe<? extends U>> t)
        throws NullPointerException {
      @SuppressWarnings("unchecked")
      Transformer<T, Maybe<U>> t1 = (Transformer<T, Maybe<U>>) t;
      return t1.transform(this.item);
    }

    @Override
    public <S> Object orElse(S s) {
      return this.item;
    }

    @Override
    public T orElseGet(Producer<? extends T> p) {
      return this.item;
    }

    @Override
    public void ifPresent(Consumer<? super T> c) {
      c.consume(this.item);
    }
  }
}
