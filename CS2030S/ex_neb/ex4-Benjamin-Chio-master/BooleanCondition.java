/**
 * A conditional statement that returns either true of false.
 * CS2030S Exercise 4
 * AY25/26 Semester 2
 *
 * @author Benjamin Chio (Lab 14A)
 */
interface BooleanCondition<T> {
  abstract boolean test(T t);
}
