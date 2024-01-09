package unit

import org.mockito.ArgumentMatchers.any
import org.mockito.MockitoSugar.{mock, reset, when}
import org.scalacheck.{Arbitrary, Gen, Shrink}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks.forAll

class PersonValidatorSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

  private val personDetailsValid = PersonDetail("Shehzad", "Ismail", "07666554423")
  private val personDetailsInvalid = PersonDetail("", "Ismail", "07666554423")
  private val personDetailsInvalid2 = PersonDetail("a" * 41, "Ismail", "07666554423")

  private val personDetailsValid1 = PersonDetail("Shehzad", "Ismail", "07666554423")
  private val personDetailsInvalid1 = PersonDetail("Shehzad", "", "07666554423")
  private val personDetailsInvalid21 = PersonDetail("Shehzad", "a" * 41, "07666554423")
  private val pdNoPhoneNumber = PersonDetail("Shehzad", "a" * 39, "")
  private val pdBadPhoneNumber = PersonDetail("Shehzad", "a" * 39, "500-call-me")

  // Mockito stuff
  private val mockPersonLookupService = mock[PersonLookupService]
  private val personValidator = new PersonValidator(mockPersonLookupService)
  private val personGeneratorValid: Arbitrary[PersonDetail] = Arbitrary(
    for{
      firstName <- Gen.alphaStr.suchThat(_.nonEmpty)
      lastName <- Gen.alphaStr.suchThat(_.nonEmpty)
    } yield {
      PersonDetail(firstName.take(40), lastName.take(40), "12345678894")
    }
  )

  private val personGeneratorInvalid: Arbitrary[PersonDetail] = Arbitrary(
    for{
      firstName <- Gen.alphaStr.suchThat(_.length > 40)
      lastName <- Gen.alphaStr.suchThat(_.length > 40)
    } yield {
      PersonDetail(firstName, lastName, "abscdgd1234")
    }
  )

  private val phoneRegex = """^[0-9 ()+--]{1,24}$"""

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

  // TODO: Fix this reset functionality
  override def beforeEach(): Unit = {
    reset(mockPersonLookupService)
  }

  "validate for firstName" must {
    behave like validateName(personDetailsValid, personDetailsInvalid, personDetailsInvalid2)
  }

  "validate for lastName" must {
    behave like validateName(personDetailsValid1, personDetailsInvalid1, personDetailsInvalid21)
  }

  // Property-based test for valid values:-
  "validate" must {

    /*
      PROPERTY-BASED TESTS
      Example properties:-
      The validator should accept as valid any first name that is non-empty and less than 41 characters in length.
      The validator should reject as invalid any first name more than 40 characters in length.
      The validator should accept as valid any phone number that meets the specified regex.
    */

    "return no errors for VALID values: method 1: using built-in generators" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(true)
      forAll(personGeneratorValid.arbitrary){
        personDetailGenerated =>
          val person1 = personValidator.validate(personDetailGenerated)
          val expectedPerson1 = Nil
          person1 mustBe expectedPerson1
      }
    }

    "return errors for INVALID values" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(true)
      forAll(personGeneratorInvalid.arbitrary){
        invalidPersonDetailsGenerated =>
          val person1 = personValidator.validate(invalidPersonDetailsGenerated)
          val expectedPerson1 = Seq("Error2")
          person1 mustBe expectedPerson1
      }
    }
  }

  "validate phone number" must {
    "return Nil when phone number is valid" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(true)
      personValidator.validate(personDetailsValid1) mustBe Nil
    }

    "return Seq(error) when phone number is invalid - no phone number present" in {
      personValidator.validate(pdNoPhoneNumber) mustBe Seq("No phone number present")
    }

    // phoneNumber: matching regex: """^[0-9 ()+--]{1,24}$""" (use string.matches(regex))
    "return Seq(error) when phone number is invalid - doesn't match regular expression" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(true)
      personValidator.validate(pdBadPhoneNumber) mustBe Seq("Malformed phone number")
    }
  }

  "validate" must {
    "return an empty List when matching passes in called service" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(true)
      val validPerson = personValidator.validate(personDetailsValid1)
      validPerson mustBe List()
    }

    "return Seq(Error) when matching fails in called service" in {
      when(mockPersonLookupService.isValid(any())).thenReturn(false)
      val validPerson = personValidator.validate(personDetailsValid1)
      validPerson mustBe Seq("Failed matching for payload")
    }
  }

  private def validateName(pd1: PersonDetail, pd2: PersonDetail, pd3: PersonDetail): Unit = {

    "contains at least one character" must {
      "return Nil as there are no errors" in {
        when(mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd1) mustBe Nil
      }
    }

    "contains no characters" must {
      "return a Seq of one error" in {
        when(mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd2) mustBe Seq("Error")
      }
    }

    "contain a maximum of 40 characters" must {
      "return a Seq of one error" in {
        when(mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd3) mustBe Seq("Error2")
      }
    }
  }
}
