/**
 * The Transformer interface that can transform a type T
 * to type U.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
package cs2030s.fp;

public interface Transformer<T, U> {
  abstract U transform(T t);
}
