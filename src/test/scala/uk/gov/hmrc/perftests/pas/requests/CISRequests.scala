/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.pas.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object CISRequests extends ServicesConfiguration with BaseRequests {

  val loginToAuthWizard: HttpRequestBuilder =
    http("Login to auth wizard")
      .get(s"$baseUrl$redirectUrl")
      .check(status.is(303))
      .check(header("Location").is(s"$redirectUrl$cisAppealablePenalties"))

  val navigateToBeforeYouStart: HttpRequestBuilder =
    http("Navigate to Before You Start page")
      .get(s"$baseUrl$redirectUrl$cisBeforeYouStart")
      .check(status.is(200))
      .check(regex("Before you start"))

  val navigateToWhyYouAreAppealing: HttpRequestBuilder =
    http("Navigate to Why You Are Appealing page")
      .get(s"$baseUrl$redirectUrl$cisWhyYouAreAppealing")
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Why are you appealing this penalty?"))

  val selectReasonForAppeal: HttpRequestBuilder =
    http("Select None of these on Why You Are Appealing page")
      .get(s"$baseUrl$redirectUrl$cisWhyYouAreAppealing")
      .formParam("csrfToken", "#{csrfToken}")
      .formParam("value", "value_10")
      .check(regex("Why are you appealing this penalty?"))
      .check(status.is(200))

  val navigateToExcuseEndDateKnown: HttpRequestBuilder =
    http("Navigate to Excuse End Date Known page")
      .get(s"$baseUrl$redirectUrl$excuseEndDateKnown")
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you know when your reasonable excuse ended?"))

  val selectExcuseEndDateKnown: HttpRequestBuilder =
    http("Select Yes on Excuse End Date Known page")
      .get(s"$baseUrl$redirectUrl$excuseEndDateKnown")
      .formParam("csrfToken", "#{csrfToken}")
      .formParam("value", "true")
      .check(regex("Do you know when your reasonable excuse ended?"))
      .check(status.is(200))

  val navigateToReasonableExcuseEndDate: HttpRequestBuilder =
    http("Navigate to Reasonable Excuse End Date page")
      .get(s"$baseUrl$redirectUrl$reasonableExcuseEndDate")
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("When did your reasonable excuse end?"))

  val enterReasonableExcuseEndDate: HttpRequestBuilder =
    http("Enter Reasonable Excuse End Date")
      .post(s"$baseUrl$redirectUrl$reasonableExcuseEndDate")
      .formParam("csrfToken", "#{csrfToken}")
      .formParam("value.day", "28")
      .formParam("value.month", "10")
      .formParam("value.year", "2026")
      .check(status.is(303))
}
