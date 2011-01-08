package com.rossabaker

import scala.collection.immutable.Vector
import scala.math._

package object scalabench {
  def time[A](f: =>A): TimedResult[A] = {
    val start = System.nanoTime
    val result = f
    val elapsed = System.nanoTime - start
    TimedResult(result, elapsed)
  }

  def repeat[A](count: Int)(f: =>A): Seq[A] = {
    var result: List[A] = Nil
    for (i <- 0 until count)
      result = f :: result
    result.reverse
  }

  def benchmark(warmupTrials: Int, timedTrials: Int)(f: =>Any): Seq[Long] = {
    repeat(warmupTrials)(f)
    repeat(timedTrials)(time(f).nanos)
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

  def report(nanos: Seq[Long]) {
    val seconds = nanos map { _ / 1e9 }
    println("Mean:      %15.9f s".format(mean(seconds)))
    println("Min:       %15.9f s".format(seconds.min))
    println("25th %%ile: %15.9f s".format(percentile(seconds, 0.25)))
    println("Median:    %15.9f s".format(percentile(seconds, 0.5)))
    println("75th %%ile: %15.9f s".format(percentile(seconds, 0.75)))
    println("Max:       %15.9f s".format(seconds.max))
  }
}

package scalabench {
  case class TimedResult[+A](result: A, nanos: Long)
}
