# Scalabench

Scalabench is a utility for running microbenchmarks in Scala.  It is here to replace the functions you write over and over again in your REPL, not to replace your profiling tool.

## Usage

### Import

The framework is available through a package object:

    import com.rossabaker.scalabench._

### time

Executes a block.  Returns a case class with its result and the number of nanoseconds it took to run.

    scala> val xs = scala.util.Random.shuffle(1 to 10000 toList)
    xs: List[Int] = List(7600, 5918, 1862, 1266, 331, 4342, 1798,

    scala> time { xs.sorted }                                   
	res0: TimedResult[List[Int]] = TimedResult(List(1, 2, ..., 10000),92164651) 

### benchmark

Takes a number of untimed warmup trials, a number of timed trials, and a block. Returns a list of the nanoseconds elapsed to execute the block per timed trial.

    scala> benchmark(100,1000) { xs.sorted }  
    res1: List[Long] = List(2254514, 2321843, 2386586, 2286572, 2251094, ...)

### report

Takes the output of benchmark, and prints some statistics on the results:

    scala> report(res1)
	Mean:          0.005289402 s
	Min:           0.002092340 s
	25th %ile:     0.002224026 s
	Median:        0.002305149 s
	75th %ile:     0.002403889 s
	Max:           1.167523561 s

## TODO

- Standard deviation
- Outlier detection 
- Parallel execution of trials
