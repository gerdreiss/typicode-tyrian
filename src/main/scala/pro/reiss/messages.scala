package pro.reiss

import io.circe.parser.*
import tyrian.http.*

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
  case DisplayPost(user: User, post: Post) extends Msg

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
