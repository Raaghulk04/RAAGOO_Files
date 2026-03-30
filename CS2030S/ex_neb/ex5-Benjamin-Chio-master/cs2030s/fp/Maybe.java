package cs2030s.fp;

/**
 * CS2030S Exercise 5
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
// :vert term -> vertical split terminal

// ONLY for Overriding methods, hierarchy of 
// private <: default <: protected <: public
      
// NO MATTER ACCESS MODIFIER ON CLASS,
// 1. class default -> protected; 
//    interface default -> public
// 2. i.e., modifier on class has NO RELATION
//    with field/method modifiers

// FINAL modifier:
// 1. Classes -> Prevents inheritance
// 2. Methods -> Prevents overriding/modifying
// 3. Fields -> Prevents change

public abstract class Maybe<T> {
  protected final static class None extends Maybe<Object> {
    @Override
    public String toString() {
      return "[]";
    }
    @Override
    public boolean equals(Object o) {
      if (o instanceof None) {
        return true;
      }
      return false;
    }
    @Override 
    protected <U extends Object> U get() throws NoSuchElementException {
      throw new NoSuchElementException();
    }
    @Override
    public Maybe<Object> filter(BooleanCondition<? super Object> bc) {
      return none();
    }
    @Override
    public <R> Maybe<R> map(Transformer<? super Object, ? extends R> trans) {
      return none();
    }
    @Override
    public <R> Maybe<R> flatMap(Transformer<? super Object, ? extends Maybe<? extends R>> trans) {
      return none();
    }
    @Override
    public Object orElse(Object o) {
      return o;
    }
    @Override
    public Object orElseGet(Producer<? extends Object> p) {
      return p.produce();
    }
    @Override
    public void ifPresent(Consumer<? super Object> c) {
      return;
    }
  }
  protected final static class Some<T> extends Maybe<T> {
    private final T val;

    protected Some(T t) {
      this.val = t;
    }

    @Override
    public String toString() {
      String v = val == null ? "null" : val.toString();
      return "[" + v + "]";
    }
    @Override
    public boolean equals(Object o) {
      // Since this is after type erasure
      // we cannot check if instanceof generic class
      if (o instanceof Some) {
        Some<?> other = (Some<?>) o;
        return this.val == null
          ? other.val == null
          : this.val.equals(other.val);
      }
      return false;
    } 
    @Override 
    protected <U extends T> U get() {
      @SuppressWarnings("unchecked")
      U u = (U) this.val;
      return u;
    }
    @Override
    public Maybe<T> filter(BooleanCondition<? super T> bc) {
      if (this.val == null || bc.test(this.val)) {
        return this;
      }
      return none();
    }
    @Override
    public <R> Maybe<R> map(Transformer<? super T, ? extends R> trans) {
      // return type of Maybe object is inferred;
      // due to declaration, trans.transform(this.val)) <: R
      
      // hence some(...) compiles
      return some(trans.transform(this.val));
    }
    @Override
    public <R> Maybe<R> flatMap(Transformer<? super T, ? extends Maybe<? extends R>> trans) {
      @SuppressWarnings("unchecked")
      Maybe<R> m = (Maybe<R>) trans.transform(this.val);
      return m;
    }
    @Override
    public T orElse(T t) {
      return this.val;
    }
    @Override
    public T orElseGet(Producer<? extends T> p) {
      return this.val;
    }
    @Override
    public void ifPresent(Consumer<? super T> c) {
      c.consume(this.val);
      return;
    }
  }

  private final static None chosenOne = new None();
  public final static <U> Maybe<U> none() {
    // SuppressWarnings only for declarations
    @SuppressWarnings("unchecked")
    Maybe<U> m = (Maybe<U>) chosenOne;
    return m;
  }
  // Due to test cases, MUST ONLY have ONE DECLARED GENERIC TYPE
  public final static <T> Maybe<T> some(T t) {
    return new Some<>(t);
  }
  public final static <U, T extends U> Maybe<U> of(T t) {
    if (t == null) {
      return none();
    }
    @SuppressWarnings("unchecked")
    Maybe<U> m = (Maybe<U>) new Some<>(t);
    return m;
  }
  protected abstract <U extends T> U get() throws NoSuchElementException;
  public abstract Maybe<T> filter(BooleanCondition<? super T> bc);
  public abstract <R> Maybe<R> map(Transformer<? super T, ? extends R> trans);
  public abstract <R> Maybe<R> flatMap(Transformer<? super T, ? extends Maybe<? extends R>> trans);
  public abstract T orElse(T t);
  public abstract T orElseGet(Producer<? extends T> p);
  public abstract void ifPresent(Consumer<? super T> c);
}
