package cs2030s.fp;

public class Lazy<T> {
  private Producer<? extends T> producer;
  private Maybe<T> value = Maybe.none();

  public Lazy(T v) {
    this.value = Maybe.some(v);
   }

  public Lazy(Producer<? extends T> s) {
    this.producer = s;
  }

  public static <T> Lazy<T> of(T v) {
    return new Lazy<>(v);
  }

  public static <T> Lazy<T> of(Producer<? extends T> s) {
    return new Lazy<>(s);
  }

  public T get() {
    return this.value
               .orElseGet((Producer<? extends T>) () -> {
                 this.value = Maybe.of(this.producer.produce());
                 return this.value.orElse(null);
               });
  }

  public <U> Lazy<U> map(Transformer<? super T, ? extends U> trans) {
    return new Lazy<>((Producer<? extends U>) () -> trans.transform(this.get()));
  }

  public <U> Lazy<U> flatMap(Transformer<? super T, ? extends Lazy<? extends U>> trans) {
    return new Lazy<>((Producer<? extends U>) () -> trans.transform(this.get()).get());
  }

  public Lazy<Boolean> filter(BooleanCondition<? super T> bc) {
    return new Lazy<>((Producer<? extends Boolean>) () -> bc.test(this.get()));
  }

  public <S, R> Lazy<R> combine(Lazy<? extends S> l2, Combiner<? super T, ? super S, ? extends R> combi) {
    return new Lazy<>((Producer<? extends R>) () -> combi.combine(this.get(), l2.get()));
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Lazy) {
      Lazy<?> l = (Lazy<?>) o;
      return this.get().equals(l.get());
    }
    return false;
  }

  @Override
  public String toString() {
    return this.value
               .map((Transformer<T, String>) val -> String.valueOf(val))
               .orElse("?");  
  }
}
