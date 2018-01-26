package model

import javax.inject.Inject

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ ExecutionContext, Future }


case class Specie(name: String, id: Long = 0L) {
  override def toString() = name
}

class SpecieTable(tag: Tag) extends Table[Specie](tag, "specie") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = (name, id).mapTo[Specie]
}

class SpecieInterface @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[slick.jdbc.JdbcProfile] {

 
  def listSpecies: Future[Seq[Specie]] = {
    db.run(DataStorage.specieTable.result)
  }

}
