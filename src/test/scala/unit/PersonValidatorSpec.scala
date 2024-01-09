package unit

import org.scalacheck.Shrink
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PersonValidatorSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny // Stop scalacheck from auto-shrinking

  override def beforeEach(): Unit = {
  }
}
