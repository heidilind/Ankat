package model

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}
import org.joda.time.DateTime
import java.sql.Timestamp


case class Sighting( 
    description: String,
    specie: String, 
    ts: DateTime,
    count: Int,
    id: Long = 0L) {
  
  override def toString() = {
    s"id: $id,\n" +
    s"description: $description,\n" +
    s"specie: $specie,\n" +
    s"sighting made: $ts,\n" +
    s"count: $count" 
  }
}

class SightingTable(tag: Tag) extends Table[Sighting](tag, "sighting") {
  
  implicit val jodaDateTimeType =
      MappedColumnType.base[DateTime, Timestamp](
        dt => new Timestamp(dt.getMillis),
        ts => new DateTime(ts.getTime))

    
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def specieName = column[String]("specie")
  def ts = column[DateTime]("ts")
  def description = column[String]("description")
  def count = column[Int]("count")

  def * = (description, specieName, ts, count, id).mapTo[Sighting]
  
  def specie = foreignKey("specie_fk", specieName, DataStorage.specieTable)(_.name, onDelete=ForeignKeyAction.Cascade)
}



class SightingInterface @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit executionContext: ExecutionContext)
                                  extends HasDatabaseConfigProvider[slick.jdbc.JdbcProfile] {

  
  val sighting = DataStorage.sightingTable

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
