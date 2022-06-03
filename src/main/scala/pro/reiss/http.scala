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
