### N-Squares

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
gradle test
```

##### With Maven
```
mvn test
```

