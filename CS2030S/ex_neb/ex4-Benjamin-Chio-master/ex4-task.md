# Exercise 4: Box

- Deadline: 10 March, 2026, Tuesday, 12:01, SGT
- Difficulty Level: 3

## Prerequisite:

- Caught up to Unit 26 of Lecture Notes
- Familiar with CS2030S Java style guide

## A Box

In this exercise, we are going to build our own generic wrapper class,
a `Box<T>`.  This is a wrapper class that can be used to store an item
of any reference type.  For this exercise, our `Box<T>` is not going to
be a very useful abstraction.  The goal is to practice:

- generic types, variance, and wildcards - factory methods

In the following, we will gradually build up the `Box<T>` class along
with some additional interfaces.  We suggest that you develop your class
step-by-step in the order below.

## The Basics

Build a generic class `Box<T>` that

- contains a `private final` field of type `T` to store the item in the
box.

- overrides the `equals` method from `Object` to compare if two boxes
are the same.  Two boxes are the same if the items in the boxes are
equal to each other, as decided by their respective `equals` method.
Your `equals` method should handle `null` arguments correctly.

- overrides the `toString` method so it returns the string representation
of its item, between `[` and `]`.

- provides a class method called `of` that returns a box with a given
object.  If `null` is passed into `of`, then a `null` should be returned.

The method `of` is called a _factory method_.  A factory method is a
method provided by a class for the creation of an instance of the class.
Using a public constructor to create an instance necessitates calling
`new` and allocating a new object on the heap every time.  A factory
method, on the other hand, allows the flexibility of reusing the same
instance.  The `of` method does not reuse instances.  You will write
another one that reuses available instances in the next section.

With the availability of the `of` factory method, `Box<T>` should keep
the constructor private.

The sequence below shows how we can use a `Box` using the methods you
developed above.

```
jshell> Box.of(4)
$.. ==> [4]
jshell> Box.of(4).equals(Box.of(4))
$.. ==> true
jshell> Box.of(4).equals(4)
$.. ==> false
jshell> Box.of(Box.of(0)).equals(Box.of(0))
$.. ==> false
jshell> Box.of(Box.of(0)).equals(Box.of(Box.of(0)))
$.. ==> true
jshell> Box.of("string")
$.. ==> [string]
jshell> Box.of("string").equals(Box.of(4))
$.. ==> false
jshell> Box.of("string").equals(Box.of("null"))
$.. ==> false
jshell> Box.of(null)
$.. ==> null
```

You can test your `Box<T>` more comprehensively by running:
```
java -cp ex4-test.jar:. Test1
```

All tests should print `ok`.

## An Empty Box

The `of` method returns a `null` if it is given a `null`. An alternative
(some might say, cleaner) design is to make our factory method return
an empty box instead if we try to create a box of `null`.

Add a class method in `Box` called `empty()` that creates and returns
an empty box, i.e., a box with a `null` item stored in it.

Since empty boxes are likely common, we want to _cache_ and reuse the
empty box, that is, create one as a private final class field called
`EMPTY_BOX`, and whenever we need to return an empty box, `EMPTY_BOX`
is returned.

What should the type of `EMPTY_BOX` be? The type should be general enough
to hold a box of any type (`Box<Shop>`, `Box<Circle>`, etc). `EMPTY_BOX`
should, therefore, be assigned the supertype of all `Box<T>` types.

Your method `empty()` should do nothing more than to type-cast `EMPTY_BOX`
to the correct type (i.e., to `Box<T>`) before returning, to ensure
type consistency.

If you find yourself in a situation where the compiler generates an
unchecked type warning, but you are sure that your code is type-safe,
you can use `@SuppressWarnings("unchecked")` (responsibly) to suppress
the warning.

Add a boolean method `isPresent` that returns `true` if the box contains
something; `false` if the box is empty.

Finally, add a class factory method called `ofNullable`, which behaves
just like `of` if the input is non-null, and returns an empty box if
the input is `null`.

Here is how the `Box` class can be used with the added methods above:

```
jshell> Box.ofNullable(4)
$.. ==> [4]
jshell> Box.ofNullable("string")
$.. ==> [string]
jshell> Box.ofNullable(null)
$.. ==> []
jshell> Box.empty() == Box.empty()
$.. ==> true
jshell> Box.ofNullable(null) == Box.empty()
$.. ==> true
jshell> Box.ofNullable(null).equals(Box.empty())
$.. ==> true
jshell> Box.ofNullable(null).equals(Box.of(null))
$.. ==> false
jshell> Box.ofNullable("string").isPresent()
$.. ==> true
jshell> Box.ofNullable(null).isPresent()
$.. ==> false
```

You can test the additions to `Box` above more comprehensively by
running:
```
java -cp ex4-test.jar:. Test2
```

All tests should print `ok`.

## Checking the Content of the Box

So far, we can only keep an item inside our `Box`, which is not very
exciting.  In the rest of the lab, we will expand `Box` to support
operations on the item inside.

Let's start by writing a generic interface called `BooleanCondition<T>`
with a single abstract boolean method `test`.  The method `test` should
take a single argument of type `T`.

Now, one can create a variety of classes by implementing this interface.
By implementing the method `test` differently, we can create different
conditions and check if the item contained in the box satisfies a given
condition or not.

Create a method `filter` in `Box` that takes in a `BooleanCondition` as
a parameter.  The method `filter` should return an empty box if the item
in the box failed the test (i.e., the call to `test` returns `false`).
Otherwise, `filter` leaves the box untouched and returns the box as it is.
Calling `filter` on an empty box just returns an empty box.

Here is an example of how `BooleanCondition<T>` can be used with `Box<T>`.
Note that we make use of the class `Number`, a superclass of `Integer`,
below.

Refer to the sample `jshell` output below.  If you run `jshell`, remember to
load all classes that `Box` depends on (such as `BooleanCondition`) before
`Box`.

```
jshell> class AlwaysTrue<T> implements BooleanCondition<T> {
   ...>   public boolean test(T t) { return true; }
   ...> }
jshell> class AlwaysFalse<T> implements BooleanCondition<T> {
   ...>   public boolean test(T t) { return false; }
   ...> }

jshell> Box.of(4).filter(new AlwaysTrue<>());
$.. ==> [4]
jshell> Box.empty().filter(new AlwaysTrue<>());
$.. ==> []

jshell> Box.of("string").filter(new AlwaysFalse<>());
$.. ==> []
jshell> Box.empty().filter(new AlwaysFalse<>());
$.. ==> []

jshell> class IntValueIsPositive implements BooleanCondition<Number> {
   ...>   public boolean test(Number t) { return t.intValue() > 0; }
   ...> }

jshell> Box.<Double>ofNullable(8.8).filter(new IntValueIsPositive());
$.. ==> [8.8]
jshell> Box.<Long>ofNullable(-100L).filter(new IntValueIsPositive());
$.. ==> []
jshell> Box.<Double>ofNullable(8.8).filter(new IntValueIsPositive()).filter(new IntValueIsPositive())
$.. ==> [8.8]
jshell> Box.<Long>ofNullable(-100L).filter(new IntValueIsPositive()).filter(new IntValueIsPositive());
$.. ==> []
```

You can test the additions to `Box` above more comprehensively by
running:
```
java -cp ex4-test.jar:. Test3
```

All tests should print `ok`.

## Implement Your Own Conditions

The test cases above show you how you could create a class that implements
a `BooleanCondition`.  Now you should implement your own.

Create a class called `IsDivisibleBy` that implements `BooleanCondition`
on `Integer` that checks if a given integer is divisible by another
integer.  The `test` method should return `true` if it is divisible;
return `false` otherwise.

Create another class called `IsLongerThan` that implements
`BooleanCondition` on `String` that checks if a given string is longer
than a given limit.  The `test` method should return `true` if it is
longer; return `false` otherwise.

Refer to the sample `jshell` output to see how it should work.  If you run
`jshell`, remember to load all classes that `Box` depends on (such as
`BooleanCondition`, `IsDivisibleBy`, and `IsLongerThan`) before `Box`.

```
jshell> new IsDivisibleBy(5).test(4);
$.. ==> false
jshell> new IsDivisibleBy(5).test(10);
$.. ==> true

jshell> Box.of(10).filter(new IsDivisibleBy(2));
$.. ==> [10]
jshell> Box.of(3).filter(new IsDivisibleBy(2));
$.. ==> []
jshell> Box.<Integer>empty().filter(new IsDivisibleBy(10));
$.. ==> []

jshell> Box.of("").filter(new IsLongerThan(10));
$.. ==> []
jshell> Box.of("123456789").filter(new IsLongerThan(10));
$.. ==> []
jshell> Box.of("1234567890").filter(new IsLongerThan(10));
$.. ==> []
jshell> Box.of("1234567890A").filter(new IsLongerThan(10));
$.. ==> [1234567890A]
jshell> Box.<String>empty().filter(new IsLongerThan(10));
$.. ==> []
```

On the other hand, calling
```
Box.of("hello").filter(new IsDivisibleBy(10));
```

should result in an "incompatible types" compilation error.  If it
compiles, your generic types are too permissive.

You can test your additions to `Box` more comprehensively by running:
```
java -cp ex4-test.jar:. Test4
```

All tests should print `ok`.

## Transforming a Box

Now, we are going to write an interface (along with its implementations)
and a method in Box that allows a box to be transformed into another box,
possibly containing a different type.

First, create an interface called `Transformer<T, U>` with an abstract
method called `transform` that takes in an argument of generic type `T`
and returns a value of generic type `U`.

Write a method called `map` in the class `Box` that takes in a
`Transformer`, and use the given `Transformer` to transform the box
(and the item inside) into another box of type `Box<U>`.

Calling `map` on an empty box should just return an empty box.

If the given `Transformer` returns a `null`, `map` returns an empty box.

In addition, implement your own `Transformer` in a non-generic class
called `LastDigitsOfHashCode` to transform the item in the box into
a box of integer, the value of which is the last $k$ digits of the
hash code returned by calling `hashCode()` on the item in the original
box (ignoring the positive/negative sign and leading zeros).  If $k$
is larger than the number of digits in the hash code, return the hash
code (ignoring the positive/negative sign).  The value $k$ is passed in
through the object of `LastDigitsOfHashCode`.  The method `hashCode()`
is defined in the class `Object`.

Refer to the sample `jshell` output to see how it should work.  Remember 
to load all classes that `Box` depends on into `jshell` before `Box`.

```
jshell> class AddOne implements Transformer<Integer,Integer> {
   ...>   public Integer transform(Integer t) { return t + 1; }
   ...> }
jshell> class StringLength implements Transformer<String,Integer> {
   ...>   public Integer transform(String t) { return t.length(); }
   ...> }

jshell> Box.of(4).map(new AddOne());
$.. ==> [5]
jshell> Box.<Integer>empty().map(new AddOne());
$.. ==> []
jshell> Box<Number> b = Box.of(4).map(new AddOne());

jshell> Box.of("string").map(new StringLength());
$.. ==> [6]
jshell> Box.of("string").map(new StringLength()).map(new AddOne());
$.. ==> [7]
jshell> Box.of("string").map(new StringLength()).filter(new IsDivisibleBy(5)).map(new AddOne());
$.. ==> []
jshell> Box.of("chocolates").map(new StringLength()).filter(new IsDivisibleBy(5)).map(new AddOne());
$.. ==> [11]
jshell> Box.<String>empty().map(new StringLength());
$.. ==> []

jshell> class AlwaysNull implements Transformer<Integer,Object> {
   ...>   public Object transform(Integer t) { return null; }
   ...> }
jshell> Box.of(4).map(new AlwaysNull());
$.. ==> []

jshell> new LastDigitsOfHashCode(4).transform("string");
$.. ==> 5903
jshell> new LastDigitsOfHashCode(2).transform(1234567);
$.. ==> 67
jshell> Box.of("string").map(new LastDigitsOfHashCode(2));
$.. ==> [3]
jshell> Box.of(123456).map(new LastDigitsOfHashCode(5));
$.. ==> [23456]
jshell> Box<Number> b = Box.of(new Integer[] {8, 8, 8}).map(new LastDigitsOfHashCode(5));
```

You can test your additions to `Box` more comprehensively
by running:
```
java -cp ex4-test.jar:. Test5
```

There shouldn't be any compilation warnings or errors when you compile
`Test1.java` and all tests should print `ok`.

## Box in a Box

The `Transformer` interface allows us to transform the item in the
box from one type into any other type, including a box! You have seen
examples above where we have a box inside a box: `Box.of(Box.of(0))`.

Now, implement your own `Transformer` in a class called `BoxIt<T>` to
transform an item into a box containing the item. The corresponding type
`T` is transformed into `Box<T>`. This transformer, when invoked with
`map`, results in a new box within the box.

Refer to the sample `jshell` output to see how it should work.  Remember 
to load all classes that `Box` depends on into `jshell` before `Box`.

```
jshell> Box.of(4).map(new BoxIt<>())
$.. ==> [[4]]
jshell> Box.of(Box.of(5)).map(new BoxIt<>())
$.. ==> [[[5]]]
jshell> Box.ofNullable(null).map(new BoxIt<>())
$.. ==> []
```

You can test your `Box` by running:
```
java -cp ex4-test.jar:. Test6
```

All tests should print `ok`.

## Files

A set of empty Java files has been given to you.  You should only edit 
these files.  You must not add any additional files.

## Following CS2030S Style Guide

You should make sure that your code follows the given Java style guide:

https://nus-cs2030s.github.io/2526-s2/style.html

## `@SuppressWarnings`

`@SuppressWarnings` should be used in at most one place: inside the
`empty()` method.
