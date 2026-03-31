package cs2030s.fp;

/**
 * CS2030S Exercise 5
 * AY25/26 Semester 2
 *
 * @author Raaghul (14C)
 */
public interface Transformer<T, U> {
  public abstract U transform(T t);
}
