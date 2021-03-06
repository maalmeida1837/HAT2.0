/*
 * Copyright (C) 2016 Andrius Aucinas <andrius.aucinas@hatdex.org>
 * SPDX-License-Identifier: AGPL-3.0
 *
 * This file is part of the Hub of All Things project (HAT).
 *
 * HAT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of
 * the License.
 *
 * HAT is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General
 * Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package hatdex.hat.api.endpoints

import akka.event.LoggingAdapter
import hatdex.hat.api.TestDataCleanup
import hatdex.hat.api.actors.DalExecutionContext
import hatdex.hat.api.endpoints.jsonExamples.BundleContextExamples
import hatdex.hat.api.json.JsonProtocol
import hatdex.hat.api.models._
import hatdex.hat.authentication.HatAuthTestHandler
import hatdex.hat.authentication.authenticators.{AccessTokenHandler, UserPassHandler}
import org.specs2.mutable.Specification
import org.specs2.specification.{BeforeAfterAll, Scope}
import spray.http.HttpHeaders.RawHeader
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._
import spray.testkit.Specs2RouteTest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class BundlesContextSpec extends Specification with Specs2RouteTest with BeforeAfterAll with BundlesContext with DalExecutionContext {
  def actorRefFactory = system

  val logger: LoggingAdapter = system.log

  val ownerAuthToken = HatAuthTestHandler.validUsers.find(_.role == "owner").map(_.userId).flatMap { ownerId =>
    HatAuthTestHandler.validAccessTokens.find(_.userId == ownerId).map(_.accessToken)
  } getOrElse ("")
  val ownerAuthHeader = RawHeader("X-Auth-Token", ownerAuthToken)

  import JsonProtocol._

  override def accessTokenHandler = AccessTokenHandler.AccessTokenAuthenticator(authenticator = HatAuthTestHandler.AccessTokenHandler.authenticator).apply()

  override def userPassHandler = UserPassHandler.UserPassAuthenticator(authenticator = HatAuthTestHandler.UserPassHandler.authenticator).apply()

  def peopleService = new Person with DalExecutionContext {
    def actorRefFactory = system

    val logger: LoggingAdapter = system.log
  }

  def thingsService = new Thing with DalExecutionContext {
    def actorRefFactory = system

    val logger: LoggingAdapter = system.log
  }

  def organisationsService = new Organisation with DalExecutionContext {
    def actorRefFactory = system

    val logger: LoggingAdapter = system.log
  }

  def locationsService = new Location with DalExecutionContext {
    def actorRefFactory = system

    val logger: LoggingAdapter = system.log
  }

  def eventsService = new Event with DalExecutionContext {
    def actorRefFactory = system

    val logger: LoggingAdapter = system.log
  }

  // Prepare the data to create test bundles on
  def beforeAll() = {
    Await.result(TestDataCleanup.cleanupAll, Duration("20 seconds"))
  }

  // Clean up all data
  def afterAll() = {
//    TestDataCleanup.cleanupAll
  }

  sequential

  "Contextual Bundle Service for Tables" should {
    "Accept valid empty bundles" in {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.emptyBundle)) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo Created
            val resp = responseAs[String]
            resp must contain("emptyBundleTest1")
            resp must contain("emptyBundleTest2")
            resp must contain("emptyBundleTest3")
          }
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}").withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo OK
            val resp = responseAs[String]
            resp must contain("emptyBundleTest1")
            resp must contain("emptyBundleTest2")
            resp must contain("emptyBundleTest3")
            responseAs[ApiBundleContext].id must beSome
          }
        }

    }

    "Accept bundles with entity selectors" in {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundleSunrise)) ~>
        routes ~>
        check {
          response.status should be equalTo Created
          val resp = responseAs[String]
          logger.info(s"Got bundle creation response $resp")
          resp must contain("emptyBundleTest2-1")
          resp must contain("sunrise")
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          response.status should be equalTo OK
          val resp = responseAs[String]
          resp must contain("emptyBundleTest2-1")
          resp must contain("sunrise")
          responseAs[ApiBundleContext].id must beSome
        }

      val bundle2 = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundleKind)) ~>
        routes ~>
        check {
          response.status should be equalTo Created
          val resp = responseAs[String]
          resp must contain("emptyBundleTest3-1")
          resp must contain("person")
          responseAs[ApiBundleContext]
        }

      bundle2.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle2.id.get}")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          response.status should be equalTo OK
          val resp = responseAs[String]
          resp must contain("emptyBundleTest3-1")
          resp must contain("person")
          responseAs[ApiBundleContext].id must beSome
        }

      val bundle3 = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entitiesBundleKindName)) ~>
        routes ~>
        check {
          response.status should be equalTo Created
          val resp = responseAs[String]
          resp must contain("emptyBundleTest4-1")
          resp must contain("person")
          resp must contain("sunrise")
          responseAs[ApiBundleContext]
        }

      bundle3.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle3.id.get}")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          response.status should be equalTo OK
          val resp = responseAs[String]
          resp must contain("emptyBundleTest4-1")
          resp must contain("person")
          resp must contain("sunrise")
          responseAs[ApiBundleContext].id must beSome
        }
    }

    "Accept bundles with entity and property selectors" in {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundleProperties)) ~>
        routes ~>
        check {
          response.status should be equalTo Created
          val resp = responseAs[String]
          resp must contain("emptyBundleTest5-1")
          resp must contain("person")
          resp must contain("dynamic")
          resp must contain("BodyWeight")
          resp must contain("QuantitativeValue")
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          response.status should be equalTo OK
          val resp = responseAs[String]
          resp must contain("emptyBundleTest5-1")
          resp must contain("person")
          resp must contain("dynamic")
          resp must contain("BodyWeight")
          resp must contain("QuantitativeValue")
          responseAs[ApiBundleContext].id must beSome
        }
    }

    val testLogger = logger
    object Context extends DataSpecContextMixin with DalExecutionContext {
      val logger: LoggingAdapter = testLogger
      def actorRefFactory = system
      val propertySpec = new PropertySpec()
      val property = propertySpec.createWeightProperty
      val dataSpec = new DataSpec()

      val (dataTable, dataSubtable) = createBasicTables
      val (_, dataField, record) = populateDataReusable
      val populatedData = (dataTable, dataField, record)

      val personSpec = new PersonSpec()
      val newPerson = personSpec.createNewPerson
      newPerson.id must beSome

      val dynamicPropertyLink = ApiPropertyRelationshipDynamic(
        None, property, None, None, "test property", dataField)

      val propertyLinkId = HttpRequest(POST, s"/person/${newPerson.id.get}/property/dynamic/${property.id.get}")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, dynamicPropertyLink.toJson.toString)) ~>
        sealRoute(personSpec.routes) ~>
        check {
          eventually {
            response.status should be equalTo Created
          }
          responseAs[ApiGenericId]
        }

      val personValues = HttpRequest(GET, s"/person/${newPerson.id.get}/values")
        .withHeaders(ownerAuthHeader) ~>
        sealRoute(personSpec.routes) ~>
        check {
          eventually {
            response.status should be equalTo OK
            responseAs[String] must contain("testValue1")
            responseAs[String] must contain("testValue2-1")
            responseAs[String] must not contain ("testValue3")
          }
        }
    }

    class Context extends Scope {
      val property = Context.property
      val populatedData = Context.populatedData
    }

    "Retrieve entity-connected data without property filtering for a named entity" in new Context {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundlePerson)) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo Created
            val resp = responseAs[String]
            resp must contain("emptyBundleTest6-1")
            resp must contain("HATperson")
          }
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}/values")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo OK
            val resp = responseAs[String]
            responseAs[Seq[ApiEntity]] must not have size(0)
            resp must contain("HATperson")
            resp must contain("testValue1")
            resp must contain("testValue2-1")
            resp must not contain ("testValue3")
          }
        }
    }

    "Retrieve entity-connected data without property filtering for all entities of a kind" in new Context {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundleAllPeople)) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo Created
            val resp = responseAs[String]
            resp must contain("emptyBundleTest7-1")
            resp must contain("person")
          }
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}/values")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo OK
            val resp = responseAs[String]
            responseAs[Seq[ApiEntity]] must not have size(0)
            resp must contain("HATperson")
            resp must contain("testValue1")
            resp must contain("testValue2-1")
            resp must not contain ("testValue3")
          }
        }
    }

    "Not retrieve data from an entity with list of not matching properties" in new Context {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundlePersonNoProps)) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo Created
            val resp = responseAs[String]
            resp must contain("emptyBundleTest8-1")
            resp must contain("HATperson")
          }
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}/values")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo OK
            val resp = responseAs[String]
            responseAs[Seq[ApiEntity]] must not have size(0)
            resp must contain("HATperson")
            resp must not contain ("testValue1")
            resp must not contain ("testValue2-1")
            resp must not contain ("testValue3")
          }
        }
    }

    "Retrieve data from an entity with a specific named property" in new Context {
      val bundle = HttpRequest(POST, "/bundles/context")
        .withHeaders(ownerAuthHeader)
        .withEntity(HttpEntity(MediaTypes.`application/json`, BundleContextExamples.entityBundlePersonProps)) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo Created
            val resp = responseAs[String]
            resp must contain("emptyBundleTest9-1")
            resp must contain("HATperson")
          }
          responseAs[ApiBundleContext]
        }

      bundle.id must beSome

      HttpRequest(GET, s"/bundles/context/${bundle.id.get}/values")
        .withHeaders(ownerAuthHeader) ~>
        routes ~>
        check {
          eventually {
            response.status should be equalTo OK
            val resp = responseAs[String]
            responseAs[Seq[ApiEntity]] must not have size(0)
            resp must contain("HATperson")
            resp must contain("testValue1")
            resp must contain("testValue2-1")
            resp must not contain ("testValue3")
          }
        }
    }
  }
}

