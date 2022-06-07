package pro.reiss

import io.circe.Codec

object Domain:
  enum DisplayTarget:
    case USERS, USER, POST, ERROR

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

  final case class Todo(
      userId: Int,
      id: Int,
      title: String,
      completed: Boolean
  ) derives Codec.AsObject

  final case class Post(
      userId: Int,
      id: Int,
      title: String,
      body: String
  ) derives Codec.AsObject

  final case class Comment(
      postId: Int,
      id: Int,
      name: String,
      email: String,
      body: String
  ) derives Codec.AsObject

  final case class Users(
      users: List[User] = List.empty,
      todos: List[Todo] = List.empty,
      posts: List[Post] = List.empty,
      comments: List[Comment] = List.empty,
      error: Option[String] = None,
      displayTarget: DisplayTarget = DisplayTarget.USERS
  ) derives Codec.AsObject

  object Users:
    def empty: Users = Users()
