package dalapi.models

import dal.Tables.OrganisationsOrganisationRow

case class ApiOrganisation(
    id: Option[Int],
    name: String,
    staticProperties: Option[Seq[ApiPropertyRelationshipStatic]],
    dynamicProperties: Option[Seq[ApiPropertyRelationshipDynamic]],
    organisations: Option[Seq[ApiOrganisationRelationship]],
    locations: Option[Seq[ApiLocationRelationship]])

object ApiOrganisation {
  def fromDbModel(entity: OrganisationsOrganisationRow) : ApiOrganisation = {
    new ApiOrganisation(Some(entity.id), entity.name, None, None, None, None)
  }
}

case class ApiOrganisationRelationship(relationshipType: String, organisation: ApiOrganisation)