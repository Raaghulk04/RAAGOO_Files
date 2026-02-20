# Exercise 0: Circle and Point

- Deadline: 27 January 2026, Tuesday, 12:00 Noon

## Prerequisites

- Familiar with the CS2030S exercise guidelines
- Able to access the CS2030S programming environment
- Setup Vim and completed basic `vim` lessons
- Link your PE account to GitHub


## What you’ll practise

- Getting familiar with CS2030S exercise workflow and scripts
- Writing simple Java classes
- Applying tell-don't-ask by adding behavior to `Point`
- Using inheritance 
- Deciding whether a field/method should be a class field/method or instance field/method

## Overview: Estimating Pi using the Monte Carlo Method

The Monte Carlo method for estimating the value of Pi is as follows.We
have a square of width 2r, and within it, a circle with a radius of r.
We randomly generate k points within the square.We count how many
points fall within the circle.Suppose n points out of k fall within
the circle.Since the area of the square is 4r^2 and the area of the
circle is PI r^2, the ratio between them is Pi/4.The ratio n/k
should therefore be Pi/4, and Pi can be estimated as 4n/k.

## Skeleton Files for Exercise 0

After you accept and retrieve the skeleton files, you should see the
following files:

- Skeleton Java files: `Point.java`, `RandomPoint.java`, `Circle.java`,
`Main.java`

- Inputs and outputs for `Main`: `inputs/ex0.k.in` and `outputs/ex0.k.out`
for different values of k.

- Test program: `ex0-test.jar` containing individual unit tests to check if the
respective classes follow the expected behavior.

## Your Task

A skeleton code has been given.Your task is to complete the
implementation of the classes `Point`, `RandomPoint`, `Circle`, and `Main`,
according to the OO principles that were taught: abstraction,
encapsulation, information hiding, inheritance, tell-don't-ask.

## The `Point` class

Fill in the class `Point` with the constructor and the necessary fields.
Add a `toString` method so that a string representation as shown in the
examples below is returned.

For instance, 
```Java
new Point(0, 0).toString();
```

should return the string:
```
(0.0, 0.0)
```

We print the x and y using Java's default `double` formatting.Note the 
space after the comma.

You will need to come back to this class and add other methods later.For
now, check that your constructor and `toString` methods are correct.

Some simple tests are provided in the file `ex0-test.jar`. Note that these
test cases are not exhaustive and you are encouraged to test your `Point`
class on your own.Proceed to the next class if you are convinced your
`Point` class is correct.

```Shell
user@xncd0:~/ex0-github-username$ java -cp ex0-test.jar:. Test1
Point: new at (0, 0).. ok
Point: new at (-3.14, 1.59).. ok
```

## The `Circle` class

Most of the `Circle` class has been written for you.You need to complete
the method `contains`.The method checks if a given point is contained in
the calling `Circle` object. To complete this method according to the
tell-don't-ask principle, you will need to add a method in the `Point`
class.

Some simple tests are provided in the file `Test2.java`.These test cases
are not exhaustive and you are encouraged to test your `Circle` class
extensively.

```Shell
user@xncd0:~/ex0-github-username$ java -cp ex0-test.jar:. Test2
Circle: new at (0, 0) with radius 4).. ok
Circle centered at (0, 0) with radius 4 contains (0, 0).. ok
Circle centered at (0, 0) with radius 4 does not contain (4, 3).. ok
Circle centered at (0, 0) with radius 4 does not contain (3, 4).. ok
Circle centered at (2, -3) with radius 0.5 contains (1.8, -3.1).. ok
Circle centered at (2, -3) with radius 0.5 does not contain (1.8, -4).. ok
```

## The `RandomPoint` class

To estimate Pi using the method above, we need to use a random number
generation.A random number generator is an entity that produces one
random number after another.We, however, cannot generate a truly random
number algorithmically.We can only generate a pseudo-random number.  A
pseudo-random number generator can be initialized with a _seed_.A
pseudo-random number generator, when initialized with the same seed, always
produces the same sequence of (seemingly random) numbers.

Java provides a class `java.util.Random` that encapsulates a pseudo-random
number generator. We can create a random number generator with a seed of 1:

```Java
Random rng = new Random(1);
```

We can then call `rng.nextDouble()` repeatedly to generate random numbers 
between 0 and 1.

### Impact of Seed

If we reinitialize `rng` again with another random generator, with a
different seed,

```Java
rng = new Random(2);
```

Calling `rng.nextDouble()` produces a different sequence.But now, let's
say that you reinitialized `rng` with the seed of 1 again:

```Java
rng = new Random(1);
```

`rng` will produce the same sequence as when the seed was 1.

(Don't take our word for it.Try out the above using `jshell`)

Using a fixed seed is important for testing since the execution of the
program will be deterministic, even when random numbers are involved.

`RandomPoint` is a subclass of `Point` that represents a randomly generated
point.The random number generator that generates a random point has a
default seed of 1.There is a public method `setSeed()` that we can use to
update the seed. Here is how it can be used:

To generate a new point,

```Java
Point p = new RandomPoint(minX, maxX, minY, maxY); 
```

`minX`, `minY`, `maxX`, `maxY` represent the minimum and maximum possible x
and y values respectively, for each randomly generated point.

To set the random seed,

```Java
RandomPoint.setSeed(10);
```

Tip: What are the fields and methods that should be associated with the
class `RandomPoint` instead of an instance of `RandomPoint`?

```Shell
user@xncd0:~/ex0-github-username$ java -cp ex0-test.jar:. Test3
RandomPoint: is a subtype of Point.. ok
RandomPoint: generate a new point with default seed.. ok
RandomPoint: generate a new point with seed 10.. ok
RandomPoint: generate a new point with the same seed.. ok
RandomPoint: reset seed to 10 and generate a new point.. ok
```

### Main

`Main` is the main program to solve the problem above.The `main` method
is provided.It includes the method to read the number of points and the
seed from the standard input and to print the estimated pi value.

The method `estimatePi` is incomplete.Determine how you should declare
`estimatePi` (Should it be an instance method or a class method? Why?) 
then complete the body by generating random points and counting how many 
fall under the given circle.

Use a circle centered at (0.5,0.5) with a radius of 0.5 for this purpose.
Use `long` and `double` within `estimatePi` for computation to ensure that
you have the right precision.

### Integer vs. Floating Point Division

In Java and many other languages, using `/` on two integers results in an
integer division.Make sure one of the operands of `/` is a floating point
number if you intend to use `/` for floating point division.

To compile `Main`, 

```Shell
user@xcnd0:~/ex0-github-username$ javac Main.java
```

To run `Main` and enter the input manually, run

```Shell
user@xcnd0:~/ex0-github-username$ java Main
```

The program will pause, waiting for inputs from keyboards.Enter two
numbers. The first is the number of points. The second is the seed.

To avoid repeatedly entering the same inputs to test, you can enter the two
numbers into a text file, say, `TEST`, and then run

```Shell
user@xcnd0:~/ex0-github-username$ java Main < TEST
```

The `<` redirects the contents of TEST into standard input, as if you typed 
it.You can read more about input/output direction in the handbook.

Sample inputs and outputs have been provided and can be found under the
`inputs` and `outputs` directory.

To test your implementation of `Main` automatically against the test data
given in `inputs` and `outputs`,

```Shell
user@xcnd0:~/ex0-github-username$ /opt/course/cs2030s/test-main ex0
```

## Common Mistakes

### 1. Changes to Code Not Taking Effect

**Symptom:** You have made changes to your code, but the output or behavior
of your program remained unchanged.

**Why**: Java code needs to be compiled before you run.You need to
compile the files that you have changed first before they can take effect.

After you have made changes to multiple files, the easiest way to recompile
everything is:

```Shell
user@xcnd0:~/ex0-github-username$ javac *.java
```

`*` is a wildcard that pattern-match any string.

### 2. Constructor Point Cannot be Applied

**Symptom:** You encounter an error that looks like:

```
RandomPoint.java:12: error: constructor Point in class Point cannot be applied to given types;
```

**Why:** The constructor for the subclass should invoke the constructor of
the superclass.See the example given in the notes on `ColoredCircle` and
`Circle`.

If the constructor of the superclass is not called explicitly, Java tries
to call the default constructor of the superclass without any argument.If
no such constructor is defined, the error above is generated.
