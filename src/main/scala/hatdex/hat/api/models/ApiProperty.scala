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
package hatdex.hat.api.models

import hatdex.hat.dal.Tables._
import org.joda.time.LocalDateTime

case class ApiProperty(
    id: Option[Int],
    dateCreated: Option[LocalDateTime],
    lastUpdated: Option[LocalDateTime],
    name: String,
    description: Option[String],
    propertyType: ApiSystemType,
    unitOfMeasurement: ApiSystemUnitofmeasurement)

object ApiProperty {
  def fromDbModel(property: SystemPropertyRow)
                 (propertyType: ApiSystemType, propertyUom: ApiSystemUnitofmeasurement) : ApiProperty = {
    new ApiProperty(Some(property.id),
      Some(property.dateCreated), Some(property.lastUpdated),
      property.name, property.description,
      propertyType, propertyUom)
  }
}

case class ApiRelationship(relationshipType: String)

case class ApiPropertyRelationshipDynamic(
    id: Option[Int],
    property: ApiProperty,
    dateCreated: Option[LocalDateTime],
    lastUpdated: Option[LocalDateTime],
    relationshipType: String,
    field: ApiDataField)

object ApiPropertyRelationshipDynamic {
  // Event properties
  def fromDbModel(relationship: EventsSystempropertydynamiccrossrefRow)
                 (property: ApiProperty, field: ApiDataField): ApiPropertyRelationshipDynamic = {
    new ApiPropertyRelationshipDynamic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field)
  }

  def fromDbModel(crossref: EventsSystempropertydynamiccrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow) : ApiPropertyRelationshipDynamic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField)
  }

  // Location properties
  def fromDbModel(relationship: LocationsSystempropertydynamiccrossrefRow)
                 (property: ApiProperty, field: ApiDataField): ApiPropertyRelationshipDynamic = {
    new ApiPropertyRelationshipDynamic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field)
  }

  def fromDbModel(crossref: LocationsSystempropertydynamiccrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow) : ApiPropertyRelationshipDynamic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField)
  }
  
  // Person properties
  def fromDbModel(relationship: PeopleSystempropertydynamiccrossrefRow)
                 (property: ApiProperty, field: ApiDataField): ApiPropertyRelationshipDynamic = {
    new ApiPropertyRelationshipDynamic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field)
  }

  def fromDbModel(crossref: PeopleSystempropertydynamiccrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow) : ApiPropertyRelationshipDynamic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField)
  }
  
  // Thing Properties
  def fromDbModel(relationship: ThingsSystempropertydynamiccrossrefRow)
                 (property: ApiProperty, field: ApiDataField): ApiPropertyRelationshipDynamic = {
    new ApiPropertyRelationshipDynamic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field)
  }

  def fromDbModel(crossref: ThingsSystempropertydynamiccrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow) : ApiPropertyRelationshipDynamic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField)
  }

  // Organisation Properties
  def fromDbModel(relationship: OrganisationsSystempropertydynamiccrossrefRow)
                 (property: ApiProperty, field: ApiDataField): ApiPropertyRelationshipDynamic = {
    new ApiPropertyRelationshipDynamic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field)
  }

  def fromDbModel(crossref: OrganisationsSystempropertydynamiccrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow) : ApiPropertyRelationshipDynamic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField)
  }
}

case class ApiPropertyRelationshipStatic(
    id: Option[Int],
    property: ApiProperty,
    dateCreated: Option[LocalDateTime],
    lastUpdated: Option[LocalDateTime],
    relationshipType: String,
    field: ApiDataField,
    record: ApiDataRecord)

object ApiPropertyRelationshipStatic {
  // Event Properties
  def fromDbModel(relationship: EventsSystempropertystaticcrossrefRow)
                 (property: ApiProperty, field: ApiDataField, record: ApiDataRecord): ApiPropertyRelationshipStatic = {
    new ApiPropertyRelationshipStatic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field, record)
  }

  def fromDbModel(crossref: EventsSystempropertystaticcrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow, record: DataRecordRow) : ApiPropertyRelationshipStatic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiRecord = ApiDataRecord.fromDataRecord(record)(None)
    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField, apiRecord)
  }

  // Location Properties
  def fromDbModel(relationship: LocationsSystempropertystaticcrossrefRow)
                 (property: ApiProperty, field: ApiDataField, record: ApiDataRecord): ApiPropertyRelationshipStatic = {
    new ApiPropertyRelationshipStatic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field, record)
  }

  def fromDbModel(crossref: LocationsSystempropertystaticcrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow, record: DataRecordRow) : ApiPropertyRelationshipStatic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiRecord = ApiDataRecord.fromDataRecord(record)(None)
    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField, apiRecord)
  }
  
  // Person Properties
  def fromDbModel(relationship: PeopleSystempropertystaticcrossrefRow)
                 (property: ApiProperty, field: ApiDataField, record: ApiDataRecord): ApiPropertyRelationshipStatic = {
    new ApiPropertyRelationshipStatic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field, record)
  }

  def fromDbModel(crossref: PeopleSystempropertystaticcrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow, record: DataRecordRow) : ApiPropertyRelationshipStatic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiRecord = ApiDataRecord.fromDataRecord(record)(None)
    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField, apiRecord)
  }
  
  // Thing propertoes
  def fromDbModel(relationship: ThingsSystempropertystaticcrossrefRow)
                 (property: ApiProperty, field: ApiDataField, record: ApiDataRecord): ApiPropertyRelationshipStatic = {
    new ApiPropertyRelationshipStatic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field, record)
  }

  def fromDbModel(crossref: ThingsSystempropertystaticcrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow, record: DataRecordRow) : ApiPropertyRelationshipStatic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiRecord = ApiDataRecord.fromDataRecord(record)(None)
    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField, apiRecord)
  }

  // Organisation propertoes
  def fromDbModel(relationship: OrganisationsSystempropertystaticcrossrefRow)
                 (property: ApiProperty, field: ApiDataField, record: ApiDataRecord): ApiPropertyRelationshipStatic = {
    new ApiPropertyRelationshipStatic(Some(relationship.id), property,
      Some(relationship.dateCreated), Some(relationship.lastUpdated),
      relationship.relationshipType, field, record)
  }

  def fromDbModel(crossref: OrganisationsSystempropertystaticcrossrefRow, property: SystemPropertyRow, propertyType: SystemTypeRow,
                  propertyUom: SystemUnitofmeasurementRow, field: DataFieldRow, record: DataRecordRow) : ApiPropertyRelationshipStatic = {
    val apiUom = ApiSystemUnitofmeasurement.fromDbModel(propertyUom)
    val apiType = ApiSystemType.fromDbModel(propertyType)
    val apiProperty = ApiProperty.fromDbModel(property)(apiType, apiUom)

    val apiRecord = ApiDataRecord.fromDataRecord(record)(None)
    val apiDataField = ApiDataField.fromDataField(field)

    fromDbModel(crossref)(apiProperty, apiDataField, apiRecord)
  }
}

case class ApiSystemType(
    id: Option[Int],
    dateCreated: Option[LocalDateTime],
    lastUpdated: Option[LocalDateTime],
    name: String,
    description: Option[String])

object ApiSystemType {
  def fromDbModel(systemType: SystemTypeRow) = {
    new ApiSystemType(Some(systemType.id),
      Some(systemType.dateCreated), Some(systemType.lastUpdated),
      systemType.name, systemType.description)
  }
}

case class ApiSystemUnitofmeasurement(
    id: Option[Int],
    dateCreated: Option[LocalDateTime],
    lastUpdated: Option[LocalDateTime],
    name: String,
    description: Option[String],
    symbol: Option[String])

object ApiSystemUnitofmeasurement {
  def fromDbModel(uom: SystemUnitofmeasurementRow) = {
    new ApiSystemUnitofmeasurement(Some(uom.id),
      Some(uom.dateCreated), Some(uom.lastUpdated),
      uom.name, uom.description, uom.symbol)
  }
}
