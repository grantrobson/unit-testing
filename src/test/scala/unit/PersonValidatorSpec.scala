package unit

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PersonValidatorSpec extends AnyWordSpec with Matchers {

  private val personDetailsValid = PersonDetail("Shehzad", "Ismail", "07666554423")
  private val personDetailsInvalid = PersonDetail("", "Ismail", "07666554423")
  private val personDetailsInvalid2 = PersonDetail("a"*41, "Ismail", "07666554423")

  private val personDetailsValid1 = PersonDetail("Shehzad", "Ismail", "07666554423")
  private val personDetailsInvalid1 = PersonDetail("Shehzad", "", "07666554423")
  private val personDetailsInvalid21 = PersonDetail("Shehzad", "a"*41, "07666554423")
  private val personValidator = new PersonValidator()


  "test firstname" when {
    validateName(personDetailsValid, personDetailsInvalid, personDetailsInvalid2)
  }

  "test surname" when {
    validateName(personDetailsValid1, personDetailsInvalid1, personDetailsInvalid21)
  }

  private def validateName(pd1: PersonDetail, pd2: PersonDetail, pd3: PersonDetail) = {

      "contains at least one character" must {
        "return Nil as there are no errors" in {
          personValidator.validate(pd1) mustBe Nil
        }
      }

      "contains no characters" must {
        "return a Seq of one error" in {
          personValidator.validate(pd2) mustBe Seq("Error")
        }
      }

      "contain a maximum of 40 characters" must {
        "return a Seq of one error" in {
          personValidator.validate(pd3) mustBe Seq("Error2")
        }
      }
    }
}
