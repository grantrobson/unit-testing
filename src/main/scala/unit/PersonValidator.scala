package unit

import netscape.javascript.JSObject

import javax.inject.Inject

/*
  Example to illustrate the following aspects of unit tests using Scala:-
    TDD, creating tests, mockito & property-based testing.
 */

trait PersonLookupService {
  /*
    personDetails: Json in form:-
      { "firstName": "bill", "lastName": "bloggs", "phoneNumber": "27838727" }
    Returns true if phone number when looked up matches person name.
   */
  def isValid(personDetails: JSObject): Boolean
}

case class PersonDetail(firstName: String, lastName: String, phoneNumber: String)

class PersonValidator @Inject()(){
  /*
    Return Nil if valid or else return Seq of errors if invalid.
    Validation rules:-
      firstName & lastName: non-empty, max length 40
      phoneNumber: matching regex: """^[0-9 ()+--]{1,24}$""" (use string.matches(regex))
      when all above valid call ANOTHER service to check that name registered against phone number matches
   */
  def validate(personDetail: PersonDetail): Seq[String] = ???
}
