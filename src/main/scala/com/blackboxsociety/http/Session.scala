package com.blackboxsociety.http

import play.api.libs.json.Json
import com.blackboxsociety.security.crypto.SignedMap

sealed trait Session[Self <: Session[Self, Secret], Secret <: SignedSecret] {

  val secret: Secret

  val data: Map[String, String]

  def withFreshData(newData: Map[String, String]): Self

  def withData(newData: Map[String, String]): Self = {
    withFreshData(data ++ newData)
  }

  def set(key: String, value: String): Self = {
    withData(Map(key -> value))
  }

  def get(key: String): Option[String] = {
    data.get(key)
  }

  def clear(): Self = {
    withFreshData(Map())
  }

  def toJson: String = {
    Json.toJson(data).toString()
  }

  def signature(): String = {
    SignedMap.sign(secret.value, data)
  }

}

sealed trait SignedSecret {
  val value: String
}
case class SessionSecret(value: String) extends SignedSecret
case class FlashSecret(value: String) extends SignedSecret

case class SignedSession(secret: SessionSecret, data: Map[String, String]) extends Session[SignedSession, SessionSecret] {

  override def withFreshData(newData: Map[String, String]): SignedSession = {
    SignedSession(secret, newData)
  }

}

case class FlashSession(secret: FlashSecret, data: Map[String, String]) extends Session[FlashSession, FlashSecret] {

  override def withFreshData(newData: Map[String, String]): FlashSession = {
    FlashSession(secret, newData)
  }

}