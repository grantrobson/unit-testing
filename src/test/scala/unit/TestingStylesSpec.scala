//package unit
//
//import org.scalacheck.Gen
//import org.scalatest.flatspec.AsyncFlatSpec
//import org.scalatest.freespec.AnyFreeSpec
//import org.scalatest.matchers.must.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class TestingStylesASpec extends AnyWordSpec with Matchers {
//  "test (AWordSpec with when)" when {
//    "called with true" must {
//      "return correct value" in {
//        val test = new TestingStyles
//        test.test(true) mustBe "value 1"
//      }
//    }
//
//    "called with false" must {
//      "return correct value" in {
//        val test = new TestingStyles
//        test.test(false) mustBe "value 2"
//      }
//    }
//  }
//
//  "test (AWordSpec without when" must {
//    "assert true" in {
//      assert(true)
//    }
//  }
//}
//
//
//
//class TestingStylesBSpec extends AsyncFlatSpec {
//  "test (AsyncFlatSpec)" should "assert true" in {
//    assert(true)
//  }
//}
//
//
//
//class TestingStylesCSpec extends AnyFreeSpec {
//  "test (AnyFreeSpec) must assert true" - {
//    assert(true)
//  }
//}
