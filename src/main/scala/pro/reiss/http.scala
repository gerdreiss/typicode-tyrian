package pro.reiss

import cats.effect.IO
import tyrian.Cmd
import tyrian.http.Http
import tyrian.http.*

object HttpHelper:
  def getAllUsers: Cmd[IO, Msg] =
    Http.send(
      Request.get("https://jsonplaceholder.typicode.com/users"),
      Msg.fromUsersResponse
    )

  def getUser(userId: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId"),
      Msg.fromUserResponse
    )

  def getUserTodos(userId: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId/todos"),
      Msg.fromUserTodosResponse
    )

  def getUserPosts(userId: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/users/$userId/posts"),
      Msg.fromUserPostsResponse
    )

  def getUserTodosAndPosts(userId: Int): Cmd[IO, Msg] =
    getUserTodos(userId) |+| getUserPosts(userId)

  def getPostComments(postId: Int): Cmd[IO, Msg] =
    Http.send(
      Request.get(s"https://jsonplaceholder.typicode.com/posts/$postId/comments"),
      Msg.fromPostCommentsResponse
    )
