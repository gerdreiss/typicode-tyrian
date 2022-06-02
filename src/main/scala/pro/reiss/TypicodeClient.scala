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
    case Msg.DisplayUser(user)   => (model.copy(users = List(user)), Cmd.None)
  }

  def view(model: Users): Html[Msg] =
    div(`class` := "ui raised padded container segment")(
      h1(`class` := "ui header")(
        i(`class` := "circular users icon")(),
        text("Users")
      ) ::
        div(`class` := "ui divider")() ::
        model.users.map { user =>
          div(`class` := "ui grid")(
            div(`class` := "four wide column")(
              div(`class` := "ui card")(
                div(`class` := "content")(
                  div(`class` := "header")(a(onClick(Msg.GetUser(user.id)))(user.name)),
                  div(`class` := "description")(user.email),
                  div(`class` := "description")(user.phone),
                  div(`class` := "description")(user.website),
                  br
                )
              )
            ),
            div(`class` := "three wide column")(
              div(`class` := "ui card")(
                div(`class` := "content")(
                  div(`class` := "header")(text("Address")),
                  div(`class` := "description")(user.address.street),
                  div(`class` := "description")(user.address.suite),
                  div(`class` := "description")(user.address.city),
                  div(`class` := "description")(user.address.zipcode)
                )
              )
            ),
            div(`class` := "three wide column")(
              div(`class` := "ui card")(
                div(`class` := "content")(
                  div(`class` := "header")(text("Position")),
                  div(`class` := "description")(s"Lat: ${user.address.geo.lat}"),
                  div(`class` := "description")(s"Long: ${user.address.geo.lng}"),
                  br,
                  br
                )
              )
            ),
            div(`class` := "six wide column")(
              div(`class` := "ui card")(
                div(`class` := "content")(
                  div(`class` := "header")(text("Company")),
                  div(`class` := "description")(user.company.name),
                  div(`class` := "description")(user.company.catchPhrase),
                  div(`class` := "description")(user.company.bs),
                  br
                )
              )
            )
          )
        }
    )

  def subscriptions(model: Users): Sub[IO, Msg] = Sub.None

enum Msg:
  case UsersError(error: String)  extends Msg
  case GetAllUsers                extends Msg
  case DisplayUsers(users: Users) extends Msg
  case GetUser(id: Int)           extends Msg
  case DisplayUser(user: User)    extends Msg

object Msg:
  private val onUsersResponse: Response => Msg = { response =>
    val json = response.body

    decode[List[User]](json) match {
      case Left(error)  => Msg.UsersError(error.getMessage)
      case Right(users) => Msg.DisplayUsers(Users(users))
    }
  }
  private val onUserResponse: Response => Msg = { response =>
    val json = response.body

    decode[User](json) match {
      case Left(error) => Msg.UsersError(error.getMessage)
      case Right(user) => Msg.DisplayUser(user)
    }
  }

  private val onError: HttpError => Msg =
    e => Msg.UsersError(e.toString)

  def fromUsersResponse: Decoder[Msg] =
    Decoder[Msg](onUsersResponse, onError)

  def fromUserResponse: Decoder[Msg] =
    Decoder[Msg](onUserResponse, onError)

object HttpHelper:
  def getAllUsers: Cmd[IO, Msg] =
    Http.send(Request.get("https://jsonplaceholder.typicode.com/users"), Msg.fromUsersResponse)

  def getUser(id: Int): Cmd[IO, Msg] =
    Http.send(Request.get(s"https://jsonplaceholder.typicode.com/users/$id"), Msg.fromUserResponse)
