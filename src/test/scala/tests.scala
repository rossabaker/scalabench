package com.rossabaker.scalabench

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

class ScalabenchTest extends WordSpec with ShouldMatchers {
  "time" should {
    "return the result" in {
      val timeResult = time { 2 + 2 }
      timeResult.result should be (4)
    }
  }

  "repeat" should {
    "run the function n times" in {
      var count = 0
      repeat(10) { count += 1 }
      count should be (10)
    }

    "return its results" in {
      val results = repeat(3) { "foo" }
      results should be (List("foo", "foo", "foo"))
    }
  }

  "run" should {
    // Bummer... name clash with ScalaTest
    import com.rossabaker.scalabench.{run => sbrun}

    "run the function warmup + trials times" in {
      var count = 0
      sbrun(5, 10) { count += 1 }
      count should be (15)
    }

    "return only the times from trials" in {
      var count = 0
      val results = sbrun(5, 10) { "foo" }
      results.size should be (10)
    }
  }

  "mean" should {
    "return the mean" in {
      mean(List(2.0, 2.0, 2.0, 4.0)) should be (2.5)
    }
  }

  "percentile" should {
    val xs = List(1.0, 3.0, 2.0, 4.0, 6.0)

    "return the min for 0.0" in {
      percentile(xs, 0.0) should equal (1.0)
    }

    "return the max for 1.0" in {
      percentile(xs, 1.0) should equal (6.0)
    }

    "return the median for 0.5" should {
      percentile(xs, 0.5) should equal (3.0)
    }

    "linearly interpolate" should {
      percentile(xs, 0.2) should equal (1.8)
    }
  }
}