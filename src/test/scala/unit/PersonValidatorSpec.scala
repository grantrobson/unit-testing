package unit
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock




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

//  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny // Stop scalacheck from auto-shrinking when test fails

  val personLookupService: PersonLookupService = mock[PersonLookupService]

  override def beforeEach(): Unit = {
    when(personLookupService.isValid(any())).thenReturn(true)
  }

  " validate first name" must {
    "accept non-empty first name" in {
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("Pablo", "", "")
      sutv.validate(pd) mustBe Seq()
    }

    "return error if firstName length > 40" in {
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

    "return error if phone number not valid when looked up" in {
      when(personLookupService.isValid(any())).thenReturn(false)
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("bill", "bloggs", "27838727")
      sutv.validate(pd) mustBe Seq("Invalid phone number")
    }

    "return multiple errors with 2 invalid inputs" in {
      when(personLookupService.isValid(any())).thenReturn(false)
      val sutv = new PersonValidator(personLookupService)
      val pd = PersonDetail("", "Smith", "")
      sutv.validate(pd).toSet mustBe Set("Invalid phone number", "Must enter first name")
    }

  }

  // Property-based testing:-
  /*
  private val genPhoneNumber: Gen[String] = RegexpGen.from("[1-9]\\d?(,\\d{3})+")

  private def genName: Gen[String] = Gen.alphaStr.suchThat(n => n.length <= 40 && n.nonEmpty)

  // Above will run out of possibilities. So instead use below:-

  private val genName: Gen[String] = for {
    len <- Gen.choose(1, 40)
    str <- Gen.listOfN(len, Gen.alphaChar).map(_.mkString)
  } yield str

  "person detail using property based testing" must {
    "test correctly for valid values" in {
      val sutv = new PersonValidator(personLookupService)

      forAll(
        genName -> "validFirstName",
        genName -> "validLastName",
        genPhoneNumber -> "validPhoneNumber"
      ) { (fn, ln, pn) =>
        val pd = PersonDetail(fn, ln, pn)
        sutv.validate(pd) mustBe Seq()
      }
    }
  }

  private val personGeneratorValid: Gen[PersonDetail] = Arbitrary(
    for {
      firstName <- genName
      lastName <- genName
      phoneNumber <- genPhoneNumber
    } yield {
      PersonDetail(firstName, lastName, phoneNumber)
    }
  ).arbitrary

  "person detail using property based testing and composite gen class" must {
    "test correctly for valid values" in {
      val sutv = new PersonValidator(personLookupService)

      forAll(
        personGeneratorValid -> "personDetail"
      ) { pd =>
        sutv.validate(pd) mustBe Seq()
      }
    }
  }

  private val personGeneratorInvalid: Gen[PersonDetail] = Arbitrary(
    for {
      firstName <- Gen.alphaStr.suchThat(_.length > 40)
      lastName <- Gen.alphaStr.suchThat(_.length > 40)
    } yield {
      PersonDetail(firstName, lastName, "abscdgd1234")
    }
  ).arbitrary

  "person name using property based testing" must {
    "test correctly generating invalid data via composite generator" in {
      val sutv = new PersonValidator(personLookupService)

      forAll(
        personGeneratorInvalid
      ) { pd =>
        sutv.validate(pd) mustBe Seq("Name too long")
      }
    }
  }
  */
   


}
