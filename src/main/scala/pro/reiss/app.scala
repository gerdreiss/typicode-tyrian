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
    case Msg.UsersError(_) =>
      (model.copy(displayTarget = DisplayTarget.ERROR), Cmd.None)
    case Msg.GetAllUsers =>
      (model, HttpHelper.getAllUsers)
    case Msg.DisplayUsers(users) =>
      (users.copy(displayTarget = DisplayTarget.USERS), Cmd.None)
    case Msg.GetUser(id) =>
      (model, HttpHelper.getUser(id))
    case Msg.DisplayUser(user) =>
      (
        model.copy(displayTarget = DisplayTarget.USER, users = List(user)),
        HttpHelper.getUserTodosAndPosts(user.id)
      )
    case Msg.GetUserTodos(id) =>
      (model, HttpHelper.getUserTodos(id))
    case Msg.DisplayUserTodos(todos) =>
      (model.copy(displayTarget = DisplayTarget.USER, todos = todos), Cmd.None)
    case Msg.GetUserPosts(id) =>
      (model, HttpHelper.getUserPosts(id))
    case Msg.DisplayUserPosts(posts) =>
      (model.copy(displayTarget = DisplayTarget.USER, posts = posts), Cmd.None)
    case Msg.DisplayPost(user, post) =>
      (
        model.copy(displayTarget = DisplayTarget.POST, users = List(user), posts = List(post)),
        HttpHelper.getPostComments(post.id)
      )
    case Msg.GetPostComments(id) =>
      (model, HttpHelper.getPostComments(id))
    case Msg.DisplayPostComments(comments) =>
      (model.copy(displayTarget = DisplayTarget.POST, comments = comments), Cmd.None)
  }

  def view(model: Users): Html[Msg] = usersView(model)

  def subscriptions(model: Users): Sub[IO, Msg] = Sub.None
