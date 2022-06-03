package pro.reiss

import cats.effect.IO
import io.circe.parser.*
import tyrian.Html.*
import tyrian.*
import tyrian.http.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object TypicodeClient extends TyrianApp[Msg, Users]:

  def init(flags: Map[String, String]): (Users, Cmd[IO, Msg]) =
    (Users.empty, HttpHelper.getAllUsers)

  def update(model: Users): Msg => (Users, Cmd[IO, Msg]) = {
    case Msg.UsersError(_)       => (model, Cmd.None)
    case Msg.GetAllUsers         => (model, HttpHelper.getAllUsers)
    case Msg.DisplayUsers(users) => (users, Cmd.None)
    case Msg.GetUser(id)         => (model, HttpHelper.getUser(id))
    case Msg.DisplayUser(user) =>
      (model.copy(users = List(user)), HttpHelper.getUserTodosAndPosts(user.id))
    case Msg.GetUserTodos(id)        => (model, HttpHelper.getUserTodos(id))
    case Msg.DisplayUserTodos(todos) => (model.copy(todos = todos), Cmd.None)
    case Msg.GetUserPosts(id)        => (model, HttpHelper.getUserPosts(id))
    case Msg.DisplayUserPosts(posts) => (model.copy(posts = posts), Cmd.None)
  }

  def view(model: Users): Html[Msg] = usersView(model)

  def subscriptions(model: Users): Sub[IO, Msg] = Sub.None

enum Msg:
  case UsersError(error: String)           extends Msg
  case GetAllUsers                         extends Msg
  case DisplayUsers(users: Users)          extends Msg
  case GetUser(id: Int)                    extends Msg
  case DisplayUser(user: User)             extends Msg
  case GetUserTodos(id: Int)               extends Msg
  case DisplayUserTodos(todos: List[Todo]) extends Msg
  case GetUserPosts(id: Int)               extends Msg
  case DisplayUserPosts(posts: List[Post]) extends Msg

object Msg:
  private val onUsersResponse: Response => Msg = { response =>
    decode[List[User]](response.body) match {
      case Left(error)  => Msg.UsersError(error.getMessage)
      case Right(users) => Msg.DisplayUsers(Users(users))
    }
  }
  private val onUserResponse: Response => Msg = { response =>
    decode[User](response.body) match {
      case Left(error) => Msg.UsersError(error.getMessage)
      case Right(user) => Msg.DisplayUser(user)
    }
  }
  private val onUserTodosResponse: Response => Msg = { response =>
    decode[List[Todo]](response.body) match {
      case Left(error)  => Msg.UsersError(error.getMessage)
      case Right(todos) => Msg.DisplayUserTodos(todos)
    }
  }
  private val onUserPostsResponse: Response => Msg = { response =>
    decode[List[Post]](response.body) match {
      case Left(error)  => Msg.UsersError(error.getMessage)
      case Right(posts) => Msg.DisplayUserPosts(posts)
    }
  }

  private val onError: HttpError => Msg = e => Msg.UsersError(e.toString)

  def fromUsersResponse: Decoder[Msg]     = Decoder[Msg](onUsersResponse, onError)
  def fromUserResponse: Decoder[Msg]      = Decoder[Msg](onUserResponse, onError)
  def fromUserTodosResponse: Decoder[Msg] = Decoder[Msg](onUserTodosResponse, onError)
  def fromUserPostsResponse: Decoder[Msg] = Decoder[Msg](onUserPostsResponse, onError)

object HttpHelper:
  def getAllUsers: Cmd[IO, Msg] =
    Http.send(
      Request.get("https://jsonplaceholder.typicode.com/users"),
      Msg.fromUsersResponse
    )

  def getUser(id: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$id"),
      Msg.fromUserResponse
    )

  def getUserTodos(id: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$id/todos"),
      Msg.fromUserTodosResponse
    )

  def getUserPosts(id: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$id/posts"),
      Msg.fromUserPostsResponse
    )

  def getUserTodosAndPosts(id: Int): Cmd[IO, Msg] =
    getUserTodos(id) |+| getUserPosts(id)
