package model

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
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
  def description = column[String]("description")
  def count = column[Int]("count")

  def * = (description, count, id).mapTo[Sighting]
  
}



class SightingStorage @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit executionContext: ExecutionContext)
                                  extends HasDatabaseConfigProvider[slick.jdbc.JdbcProfile] {


  val sighting = TableQuery[SightingTable]

  def add(sight: Sighting): Future[String] = {
    db.run(sighting += sight).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    db.run(sighting.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Sighting]] = {
    db.run(sighting.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Sighting]] = {
    db.run(sighting.result)
  }

}