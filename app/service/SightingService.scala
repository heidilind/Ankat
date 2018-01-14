//package service
//
//
//import model.{Sighting, SightingStorage}
//import scala.concurrent.Future
//
//object SightingService {
//  
//  def addUser(sight: Sighting): Future[String] = {
//    SightingStorage.add(sight)
//  }
//
//  def deleteUser(id: Long): Future[Int] = {
//    SightingStorage.delete(id)
//  }
//
//  def getUser(id: Long): Future[Option[Sighting]] = {
//    SightingStorage.get(id)
//  }
//
//  def listAllUsers: Future[Seq[Sighting]] = {
//    SightingStorage.listAll
//}
//  
//  
//}