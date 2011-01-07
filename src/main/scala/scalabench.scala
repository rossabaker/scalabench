package com.rossabaker

import scala.math._

package object scalabench {
  def time[A](f: =>A): TimedResult[A] = {
    val start = System.nanoTime
    val result = f
    val elapsed = System.nanoTime - start
    TimedResult(result, elapsed)
  }

  def repeat[A](count: Int)(f: =>A): List[A] = {
    var result: List[A] = Nil
    for (i <- 0 until count)
      result = f :: result
    result
  }

  def run(warmupTrials: Int, timedTrials: Int)(f: =>Any): List[Long] = {
    repeat(warmupTrials)(f)
    repeat(timedTrials)(time(f)) map { _.nanoseconds }
  }

  def mean(xs: Seq[Double]): Double = xs.sum.toDouble / xs.size

  def percentile(xs: Seq[Double], p: Double): Double = {
    val splitAt = p * (xs.size - 1) + 1
    xs.sorted.splitAt(splitAt.toInt) match {
      case (lower, Nil) => lower.last
      case (lower, upper) =>
        val remainder = splitAt % 1.0
        (1.0 - remainder) * lower.last + remainder * upper.head
    }
  }

  def benchmark(warmupTrials: Int, timedTrials: Int)(f: =>Any) {
    val times = run(warmupTrials, timedTrials)(f) map { _ / 1e9 }
    println("Mean:      %15.9f s".format(mean(times)))
    println("Min:       %15.9f s".format(times.min))
    println("25th %%ile: %15.9f s".format(percentile(times, 0.25)))
    println("Median:    %15.9f s".format(percentile(times, 0.5)))
    println("75th %%ile: %15.9f s".format(percentile(times, 0.75)))
    println("Max:       %15.9f s".format(times.max))
  }
}

package scalabench {
  case class TimedResult[+A](result: A, nanoseconds: Long)
}
