## Auto-generated Report for ex5

Your submitted code is not correct. It failed some of the test cases (including hidden ones).
```
Maybe.<Integer>some(null).filter(isEven) returns Maybe.some(null).. failed
Maybe.<Integer>some(1).filter(isEven) returns Maybe.none().. failed
Maybe.<Integer>some(2).filter(isEven) returns Maybe.some(2).. failed
Maybe.<String>some("").map(wordToInt) returns Maybe.some(null).. failed
Maybe<Number> m = Maybe.<String>none().map(toHashCode) should compile.. failed
Maybe<Number> m = Maybe.<String>some("cs2030s").map(toHashCode) should compile.. failed
Maybe.<String>none().flatMap(wordToMaybeInt) returns Maybe.none().. failed
Maybe.<String>some("").flatMap(wordToMaybeInt) returns Maybe.none().. failed
Maybe.<String>some("one").flatMap(wordToMaybeInt) returns Maybe.some(1).. failed
Maybe<Number> m = Maybe.<String>some("one").flatMap(t) should compile.. failed
Maybe<Number> m = Maybe.<Object>some("one").flatMap(t) should compile.. failed
Maybe.<String>some("hello").flatMap(s -> Maybe.some(null)) returns Maybe.some(null).. failed
Maybe.<Number>none().orElse(4) returns 4.. failed
Maybe.<Integer>some(1).orElse(4) returns 1.. failed
Maybe.<Number>none().orElseGet(zero) returns 0.0.. failed
Maybe.<Number>some(1).orElseGet(zero) returns 1.. failed
```

### Output from Style Checker (checkstyle)

Your code has violated some style guidelines. Please adhere to the CS2030S coding style.
```
Starting audit...
[WARN] /home/b/bchiozs/ex5-Benjamin-Chio/cs2030s/fp/BooleanCondition.java:8:1: 'package' should be separated from previous statement. [EmptyLineSeparator]
[WARN] /home/b/bchiozs/ex5-Benjamin-Chio/cs2030s/fp/Producer.java:7:1: 'package' should be separated from previous statement. [EmptyLineSeparator]
[WARN] /home/b/bchiozs/ex5-Benjamin-Chio/cs2030s/fp/Transformer.java:9:1: 'package' should be separated from previous statement. [EmptyLineSeparator]
Audit done.
```

### Output from Static Code Analyzer (pmd)

Our static code analyzer uncovers the following issues (may repeat what our bot discovered):
```
./Main.java:1:	UnnecessaryImport:	Unused import 'cs2030s.fp.Maybe'
./Main.java:2:	UnnecessaryImport:	Unused import 'cs2030s.fp.Transformer'
```

### Summary

Please refer to the errors and warnings above and improve your work.  Note that there could be other issues that are not detectable by the tools we used.  The tutors will look at your code and make additional comments and suggestions to improve.

### Achievement Badge

Needs Improvement (Not Correct)
