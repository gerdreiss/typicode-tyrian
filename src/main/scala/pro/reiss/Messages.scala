package pro.reiss

import io.circe.parser.*
import tyrian.http.*

import Domain.*

enum Messages:
  case DisplayError(error: String)                  extends Messages
  case GetAllUsers                                  extends Messages
  case DisplayUsers(users: Users)                   extends Messages
  case GetUser(userId: Int)                         extends Messages
  case DisplayUser(user: User)                      extends Messages
  case GetUserTodos(userId: Int)                    extends Messages
  case DisplayUserTodos(todos: List[Todo])          extends Messages
  case GetUserPosts(userId: Int)                    extends Messages
  case DisplayUserPosts(posts: List[Post])          extends Messages
  case DisplayUserPost(user: User, post: Post)      extends Messages
  case GetPostComments(postId: Int)                 extends Messages
  case DisplayPostComments(comments: List[Comment]) extends Messages

object Messages:
  private val onUsersResponse: Response => Messages = { response =>
    decode[List[User]](response.body) match {
      case Left(error)  => Messages.DisplayError(error.getMessage)
      case Right(users) => Messages.DisplayUsers(Users(users))
    }
  }
  private val onUserResponse: Response => Messages = { response =>
    decode[User](response.body) match {
      case Left(error) => Messages.DisplayError(error.getMessage)
      case Right(user) => Messages.DisplayUser(user)
    }
  }
  private val onUserTodosResponse: Response => Messages = { response =>
    decode[List[Todo]](response.body) match {
      case Left(error)  => Messages.DisplayError(error.getMessage)
      case Right(todos) => Messages.DisplayUserTodos(todos)
    }
  }
  private val onUserPostsResponse: Response => Messages = { response =>
    decode[List[Post]](response.body) match {
      case Left(error)  => Messages.DisplayError(error.getMessage)
      case Right(posts) => Messages.DisplayUserPosts(posts)
    }
  }
  private val onPostCommentsResponse: Response => Messages = { response =>
    decode[List[Comment]](response.body) match {
      case Left(error)     => Messages.DisplayError(error.getMessage)
      case Right(comments) => Messages.DisplayPostComments(comments)
    }
  }

  private val onError: HttpError => Messages = e => Messages.DisplayError(e.toString)

  def fromUsersResponse: Decoder[Messages]        = Decoder[Messages](onUsersResponse, onError)
  def fromUserResponse: Decoder[Messages]         = Decoder[Messages](onUserResponse, onError)
  def fromUserTodosResponse: Decoder[Messages]    = Decoder[Messages](onUserTodosResponse, onError)
  def fromUserPostsResponse: Decoder[Messages]    = Decoder[Messages](onUserPostsResponse, onError)
  def fromPostCommentsResponse: Decoder[Messages] = Decoder[Messages](onPostCommentsResponse, onError)
