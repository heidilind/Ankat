package model

import slick.jdbc.PostgresProfile.api._
import slick.driver.PostgresDriver.api._
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.JdbcProfile
import play.api.db.slick.HasDatabaseConfigProvider


case class Sighting( 
    description: String, 
    count: Int,
    id: Long = 0L)

class SightingTable(tag: Tag) extends Table[Sighting](tag, "sighting") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def description = column[String]("name")
  def count = column[Int]("count")

  def * = (description, count, id).mapTo[Sighting]
  
}

object Sightings extends HasDatabaseConfigProvider[JdbcProfile]  {

  val dbConfig = DatabaseConfigProvider

  val sightings = TableQuery[SightingTable]

  def add(sight: Sighting): Future[String] = {
    dbConfig.db.run(sightings += sight).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(sightings.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Sighting]] = {
    dbConfig.db.run(sightings.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Sighting]] = {
    dbConfig.db.run(sightings.result)
  }

}