package service


import model.{Sighting, Sightings}
import scala.concurrent.Future

object SightingService {
  
  def addUser(sight: Sighting): Future[String] = {
    Sightings.add(sight)
  }

  def deleteUser(id: Long): Future[Int] = {
    Sightings.delete(id)
  }

  def getUser(id: Long): Future[Option[Sighting]] = {
    Sightings.get(id)
  }

  def listAllUsers: Future[Seq[Sighting]] = {
    Sightings.listAll
}
  
  
}