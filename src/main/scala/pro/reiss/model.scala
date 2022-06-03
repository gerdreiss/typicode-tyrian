package pro.reiss

import io.circe.Codec

final case class Geo(
    lat: String,
    lng: String
) derives Codec.AsObject

final case class Address(
    street: String,
    suite: String,
    city: String,
    zipcode: String,
    geo: Geo
) derives Codec.AsObject

final case class Company(
    name: String,
    catchPhrase: String,
    bs: String
) derives Codec.AsObject

final case class User(
    id: Int,
    name: String,
    username: String,
    email: String,
    phone: String,
    website: String,
    address: Address,
    company: Company
) derives Codec.AsObject

final case class Users(
    users: List[User] = List.empty,
    error: Option[String] = None
) derives Codec.AsObject

object Users:
  def empty: Users = Users()
