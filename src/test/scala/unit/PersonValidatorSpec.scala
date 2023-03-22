package unit

import org.mockito.ArgumentMatchers.any
import org.mockito.MockitoSugar.{mock, reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

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

  "validate phone number" must {
    "return Nil when phone number is valid" in {
      when (mockPersonLookupService.isValid(any())).thenReturn(true)
      personValidator.validate(personDetailsValid1) mustBe Nil
    }

    "return Seq(error) when phone number is invalid - no phone number present" in {
      personValidator.validate(pdNoPhoneNumber) mustBe Seq("No phone number present")
    }

    // phoneNumber: matching regex: """^[0-9 ()+--]{1,24}$""" (use string.matches(regex))
    "return Seq(error) when phone number is invalid - doesn't match regular expression" in {
      when (mockPersonLookupService.isValid(any())).thenReturn(true)
      personValidator.validate(pdBadPhoneNumber) mustBe Seq("Malformed phone number")
    }
  }

  "validate" must {
    "return an empty List when matching passes in called service" in {
      when (mockPersonLookupService.isValid(any())).thenReturn(true)
      val validPerson = personValidator.validate(personDetailsValid1)
      validPerson mustBe List()
    }

    "return Seq(Error) when matching fails in called service" in {
      when (mockPersonLookupService.isValid(any())).thenReturn(false)
      val validPerson = personValidator.validate(personDetailsValid1)
      validPerson mustBe Seq("Failed matching for payload")
    }
  }

  private def validateName(pd1: PersonDetail, pd2: PersonDetail, pd3: PersonDetail) = {

    "contains at least one character" must {
      "return Nil as there are no errors" in {
        when (mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd1) mustBe Nil
      }
    }

    "contains no characters" must {
      "return a Seq of one error" in {
        when (mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd2) mustBe Seq("Error")
      }
    }

    "contain a maximum of 40 characters" must {
      "return a Seq of one error" in {
        when (mockPersonLookupService.isValid(any())).thenReturn(true)
        personValidator.validate(pd3) mustBe Seq("Error2")
      }
    }
  }
}
