package pro.reiss

import cats.effect.IO
import tyrian.Cmd
import tyrian.http.Http
import tyrian.http.*

object TypecodeClient:
  def getAllUsers: Cmd[IO, Messages] =
    Http.send(
      Request.get("https://jsonplaceholder.typicode.com/users"),
      Messages.fromUsersResponse
    )

  def getUser(userId: Int): Cmd[IO, Messages] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId"),
      Messages.fromUserResponse
    )

  def getUserTodos(userId: Int): Cmd[IO, Messages] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId/todos"),
      Messages.fromUserTodosResponse
    )

  def getUserPosts(userId: Int): Cmd[IO, Messages] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId/posts"),
      Messages.fromUserPostsResponse
    )

  def getUserTodosAndPosts(userId: Int): Cmd[IO, Messages] =
    getUserTodos(userId) |+| getUserPosts(userId)

  def getPostComments(postId: Int): Cmd[IO, Messages] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/posts/$postId/comments"),
      Messages.fromPostCommentsResponse
    )
