package unit

import org.scalacheck.Shrink
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PersonValidatorSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {


  /*
  Shrinking
  ScalaCheck has some useful behavior where, when it finds a failing test case, it will try to find the “smallest” 
  (or perhaps “simplest”) value it can, which caused the test to fail. This can make debugging easier. 
  Imagine your algorithm fails when the length of a list is larger than, say, five elements. 
  If ScalaCheck’s first attempt is a list with 2,000 elements in it, it may take you a while to work out
   what’s going on and what the underlying issue is. So ScalaCheck shrinks the input, effectively binary searching between 
   successful and unsuccessful test runs, to see where the boundary lies.
   */

  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny // Stop scalacheck from auto-shrinking when test fails

  override def beforeEach(): Unit = {
  }

}
