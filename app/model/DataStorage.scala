package model

import slick.jdbc.PostgresProfile.api._

object DataStorage {
  val specieTable = TableQuery[SpecieTable]   
  val sightingTable = TableQuery[SightingTable]
}