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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object AuthLoginRequests extends ServicesConfiguration with BaseRequests {

  lazy val navigateToAuth: HttpRequestBuilder =
    http("Auth wizard")
      .get(CISRequests.authLoginStubUrl)
      .check(saveCsrfToken())
      .check(status.is(200))
      .check(regex("Authority Wizard").exists)

  def authLogin: HttpRequestBuilder =
    http("Login as an GG sign-in")
      .post(CISRequests.authLoginStubUrl)
      .formParam("redirectionUrl", redirectUrl)
      .formParam("csrfToken", "#{csrfToken}")
      .formParam("credentialStrength", "strong")
      .formParam("authorityId", "")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("enrolment[0].name", "HMRC-CHAR-ORG")
      .formParam("enrolment[0].taxIdentifier[0].name", "CHARID")
      .formParam("enrolment[0].taxIdentifier[0].value", "IT00071")
      .formParam("enrolment[0].state", "Activated")
      .check(status.is(303))
      .check(header("Location").is(redirectUrl))
}
