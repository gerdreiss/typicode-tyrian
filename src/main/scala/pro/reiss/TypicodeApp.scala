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
    (Users.empty, TypecodeClient.getAllUsers)

  def update(model: Users): Messages => (Users, Cmd[IO, Messages]) = {
    case Messages.UsersError(_) =>
      (model.copy(displayTarget = DisplayTarget.ERROR), Cmd.None)
    case Messages.GetAllUsers =>
      (model, TypecodeClient.getAllUsers)
    case Messages.DisplayUsers(users) =>
      (users.copy(displayTarget = DisplayTarget.USERS), Cmd.None)
    case Messages.GetUser(id) =>
      (model, TypecodeClient.getUser(id))
    case Messages.DisplayUser(user) =>
      (
        model.copy(displayTarget = DisplayTarget.USER, users = List(user)),
        TypecodeClient.getUserTodosAndPosts(user.id)
      )
    case Messages.GetUserTodos(id) =>
      (model, TypecodeClient.getUserTodos(id))
    case Messages.DisplayUserTodos(todos) =>
      (model.copy(displayTarget = DisplayTarget.USER, todos = todos), Cmd.None)
    case Messages.GetUserPosts(id) =>
      (model, TypecodeClient.getUserPosts(id))
    case Messages.DisplayUserPosts(posts) =>
      (model.copy(displayTarget = DisplayTarget.USER, posts = posts), Cmd.None)
    case Messages.DisplayPost(user, post) =>
      (
        model.copy(displayTarget = DisplayTarget.POST, users = List(user), posts = List(post)),
        TypecodeClient.getPostComments(post.id)
      )
    case Messages.GetPostComments(id) =>
      (model, TypecodeClient.getPostComments(id))
    case Messages.DisplayPostComments(comments) =>
      (model.copy(displayTarget = DisplayTarget.POST, comments = comments), Cmd.None)
  }

  def view(model: Users): Html[Messages] = Views.usersView(model)

  def subscriptions(model: Users): Sub[IO, Messages] = Sub.None
