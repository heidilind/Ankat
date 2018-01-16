package model

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}


case class Sighting( 
    description: String, 
    count: Int,
    id: Long = 0L) {
  
  override def toString() = description
}

class SightingTable(tag: Tag) extends Table[Sighting](tag, "sighting") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def description = column[String]("name")
  def count = column[Int]("count")

  def * = (description, count, id).mapTo[Sighting]
  
}

class SightingStorage @Inject() (dbConfigProvider: DatabaseConfigProvider)
                                 (implicit executionContext: ExecutionContext)
                                  extends HasDatabaseConfigProvider[slick.jdbc.JdbcProfile] {

   // private val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

  val sightings = TableQuery[SightingTable]

  def add(sight: Sighting): Future[String] = {
    db.run(sightings += sight).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    db.run(sightings.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Sighting]] = {
    db.run(sightings.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Sighting]] = {
    db.run(sightings.result)
  }

}