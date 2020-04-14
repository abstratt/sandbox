### N-Squares

[![Build Status on Travis CI](https://travis-ci.org/abstratt/sandbox.svg?branch=n-queens)](https://travis-ci.org/abstratt/sandbox)


This project attempts to solve the [N-Queens puzzle](https://en.wikipedia.org/wiki/Eight_queens_puzzle), in conjunction with the [No-three-in-line problem](https://en.wikipedia.org/wiki/No-three-in-line_problem).

#### The code

* Solver - the basic solving code.
* MTSolver - a multithreaded variant.
* Board - helps with visualizing boards and validating solutions, but not actually used in the solver code. 
* Square - a pair row+column, with some helpful utilities used in the solver code (to answer questions such as "are these two squares in a a diagonal?" and "are these three squares in line?")

The tests should help understand the expectations for those classes.

#### Running the code

##### With Gradle

```
gradle test [-Dprop1=value1] [-Dprop2=value2]
```

##### With Maven
```
mvn test [-Dprop1=value1] [-Dprop2=value2]
```


##### System properties

* nqueens.slowTests (boolean) - Some test cases take too long to complete and hence are disabled by default. In order to run them, specify the -Dnqueens.slowTests=true property.
* nqueens.debug (boolean) - Some code produces logging information which may be helpful in debugging. To enable debugging, pass "true" to this property. 

