package pages

import pages.behaviours.PageBehaviours

class DoYouKnowYourNationalInsuranceNumberPageSpec extends PageBehaviours {

  "DoYouKnowYourNationalInsuranceNumberPage" - {

    beRetrievable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)

    beSettable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)

    beRemovable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)
  }
}
