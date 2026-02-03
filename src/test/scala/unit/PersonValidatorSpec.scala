package unit

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
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

  val personLookupService: PersonLookupService = mock[PersonLookupService]

  override def beforeEach(): Unit = {}

  " validate first name" must {
    "accept non-empty first name" in {
      when(personLookupService.isValid(any())).thenReturn(true)
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("Pablo", "", "")
      sutv.validate(pd) mustBe Seq()
    }

    "return error if firstName length > 40" in {
      when(personLookupService.isValid(any())).thenReturn(true)
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("a" * 41, "", "")
      sutv.validate(pd) mustBe Seq("Name too long")
    }
    "return error if first name is empty" in {
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("", "", "")
      sutv.validate(pd) mustBe Seq("Must enter first name")
    }

  }

  " validate last name" must {


  }

  "validate phone number" must {


  }


}
