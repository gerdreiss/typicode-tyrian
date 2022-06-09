package pro.reiss

import cats.effect.IO
import io.circe.parser.*
import tyrian.Html.*
import tyrian.*
import tyrian.http.*

import scala.scalajs.js.annotation.*

import Domain.*

@JSExportTopLevel("TyrianApp")
object TypicodeApp extends TyrianApp[Messages, Users]:

  def init(flags: Map[String, String]): (Users, Cmd[IO, Messages]) =
    (Users.empty, TypicodeClient.getAllUsers)

  def update(model: Users): Messages => (Users, Cmd[IO, Messages]) = {
    case Messages.DisplayError(_) =>
      (model.copy(displayTarget = DisplayTarget.ERROR), Cmd.None)
    case Messages.GetAllUsers =>
      (model, TypicodeClient.getAllUsers)
    case Messages.DisplayUsers(users) =>
      (users.copy(displayTarget = DisplayTarget.USERS), Cmd.None)
    case Messages.GetUser(id) =>
      (model, TypicodeClient.getUser(id))
    case Messages.DisplayUser(user) =>
      (
        model.copy(displayTarget = DisplayTarget.USER, users = List(user)),
        TypicodeClient.getUserTodosAndPosts(user.id)
      )
    case Messages.GetUserTodos(id) =>
      (model, TypicodeClient.getUserTodos(id))
    case Messages.DisplayUserTodos(todos) =>
      (model.copy(displayTarget = DisplayTarget.USER, todos = todos), Cmd.None)
    case Messages.GetUserPosts(id) =>
      (model, TypicodeClient.getUserPosts(id))
    case Messages.DisplayUserPosts(posts) =>
      (model.copy(displayTarget = DisplayTarget.USER, posts = posts), Cmd.None)
    case Messages.DisplayUserPost(user, post) =>
      (
        model.copy(displayTarget = DisplayTarget.POST, users = List(user), posts = List(post)),
        TypicodeClient.getPostComments(post.id)
      )
    case Messages.GetPostComments(id) =>
      (model, TypicodeClient.getPostComments(id))
    case Messages.DisplayPostComments(comments) =>
      (model.copy(displayTarget = DisplayTarget.POST, comments = comments), Cmd.None)
  }

  def view(model: Users): Html[Messages] = Views.usersView(model)

  def subscriptions(model: Users): Sub[IO, Messages] = Sub.None
