package unit

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import org.scalacheck.{Arbitrary, Gen, Shrink}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks.forAll
import wolfendale.scalacheck.regexp.RegexpGen

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

  override def beforeEach(): Unit = when(personLookupService.isValid(any())).thenReturn(true)

  " validate first name" must {

    behave like nameTester("first name", s => PersonDetail(s, "Biggs", "12345678"))
  }

  " validate last name" must {

    behave like nameTester("last name", s => PersonDetail("Pablo", s, "12345678"))
  }

  "validate phone number" must {

    "return error if phone number not valid when looked up" in {
      when(personLookupService.isValid(any())).thenReturn(false)
      val sutv = new PersonValidator(personLookupService)
      val pd   = PersonDetail("bill", "bloggs", "27838727")
      sutv.validate(pd) mustBe Seq("Invalid phone number")
    }

    "return multiple errors with 2 invalid inputs" in {
      when(personLookupService.isValid(any())).thenReturn(false)
      val sutv = new PersonValidator(personLookupService)
      val pd   = PersonDetail("", "Smith", "")
      sutv.validate(pd).toSet mustBe Set("Invalid phone number", "Must enter first name")
    }

  }

  "PersonDetail testing using property based test" must {
    val genName: Gen[String] = for {
      len <- Gen.choose(1, 40)
      str <- Gen.listOfN(len, Gen.alphaChar).map(_.mkString)
    } yield str

    "test for valid values" in {

      val sutv = new PersonValidator(personLookupService)

      forAll(
        genName -> "Valid first name",
        genName -> "Valid last name"
      ) { (fn, ln) =>
        val pd = PersonDetail(fn, ln, "123456789")
        sutv.validate(pd).toSet mustBe Set()
      }

    }

    val genPhoneNumber: Gen[String] = RegexpGen.from("[1-9]\\d?(,\\d{3})+")

    val personGeneratorValid: Gen[PersonDetail] = Arbitrary(
      for {
        firstName   <- genName
        lastName    <- genName
        phoneNumber <- genPhoneNumber
      } yield PersonDetail(firstName, lastName, phoneNumber)
    ).arbitrary

    "test for valid values with PersonDetail generator" in {

      val sutv = new PersonValidator(personLookupService)

      forAll(
        personGeneratorValid -> "random PersonDetail"
      ) { pd =>
        sutv.validate(pd).toSet mustBe Set()
      }
    }
  }

  private def nameTester(name: String, f: String => PersonDetail): Unit = {

    "accept non-empty name" in {

      val sutv = new PersonValidator(personLookupService)
      val pd   = f("Pablo")
      sutv.validate(pd) mustBe Seq()
    }

    "return error if name length > 40" in {

      val sutv = new PersonValidator(personLookupService)
      val pd   = f("a" * 41)
      sutv.validate(pd) mustBe Seq("Name too long")
    }
    "return error if name is empty" in {

      val sutv = new PersonValidator(personLookupService)
      val pd   = f("")
      sutv.validate(pd) mustBe Seq(s"Must enter $name")
    }

  }

}
