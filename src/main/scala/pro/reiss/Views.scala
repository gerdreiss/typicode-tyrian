package pro.reiss

import tyrian.Html.*
import tyrian.*

import Domain.*

object Views:
  def usersView(model: Users): Html[Messages] =
    div(cls := "ui raised very padded container segment")(
      targetedHeader(model) :: div(cls := "ui divider")() :: targetedView(model)
    )

  def targetedHeader(model: Users): Html[Messages] =
    h1(cls := "ui header")(
      i(cls := "circular users icon")(),
      model.displayTarget match
        case DisplayTarget.USER =>
          userHeader(model.users.head.name, Messages.GetAllUsers)
        case DisplayTarget.POST =>
          userHeader(model.users.head.name, Messages.GetUser(model.users.head.id))
        case _ => div(cls := "content")(text("Users"))
    )

  def userHeader(t: String, msg: Messages): Html[Messages] =
    div(cls := "content", style("width", "92%"))(
      div(cls := "ui grid")(
        div(cls := "row")(
          div(cls := "fourteen wide column")(
            p(t)
          ),
          div(cls := "two wide column")(
            button(cls := "ui labeled button", onClick(msg))(
              i(cls := "left arrow icon")(),
              text("Back")
            )
          )
        )
      )
    )

  def targetedView(model: Users): List[Html[Messages]] =
    model.displayTarget match
      case DisplayTarget.USERS => userListView(model.users)
      case DisplayTarget.USER  => userDetailView(model.users.head, model.todos, model.posts)
      case DisplayTarget.POST  => postView(model.posts.head, model.comments)
      case DisplayTarget.ERROR => div(cls := "content")(text(model.error.get)) :: Nil

  def userListView(users: List[User]): List[Html[Messages]] =
    users.map { user =>
      div(cls := "ui grid")(
        div(cls := "four wide column")(
          div(cls := "ui card")(
            div(cls := "content")(
              div(cls := "header")(a(onClick(Messages.GetUser(user.id)))(user.name)),
              div(cls := "description")(
                i(cls := "envelope icon")(),
                text(user.email)
              ),
              div(cls := "description")(
                i(cls := "phone icon")(),
                text(user.phone)
              ),
              div(cls := "description")(
                i(cls := "globe icon")(),
                text(user.website)
              ),
              br
            )
          )
        ),
        div(cls := "three wide column")(
          div(cls := "ui card")(
            div(cls := "content")(
              div(cls := "header")(
                i(cls := "address book icon")(),
                text("Address")
              ),
              div(cls := "description")(user.address.street),
              div(cls := "description")(user.address.suite),
              div(cls := "description")(user.address.city),
              div(cls := "description")(user.address.zipcode)
            )
          )
        ),
        div(cls := "three wide column")(
          div(cls := "ui card")(
            div(cls := "content")(
              div(cls := "header")(
                i(cls := "location arrow icon")(),
                text("Position")
              ),
              div(cls := "description")(s"Lat: ${user.address.geo.lat}"),
              div(cls := "description")(s"Lng: ${user.address.geo.lng}"),
              br,
              br
            )
          )
        ),
        div(cls := "six wide column")(
          div(cls := "ui card")(
            div(cls := "content")(
              div(cls := "header")(
                i(cls := "building icon")(),
                text("Company")
              ),
              div(cls := "description")(user.company.name),
              div(cls := "description")(user.company.catchPhrase),
              div(cls := "extra content")(p(user.company.bs)),
              br
            )
          )
        )
      )
    }

  def userDetailView(user: User, todos: List[Todo], posts: List[Post]): List[Html[Messages]] =
    div(cls := "ui grid")(
      div(cls := "five wide column")(
        div(cls := "ui card")(
          div(cls := "content")(
            div(cls := "description")(
              i(cls := "envelope icon")(),
              text(user.email)
            ),
            div(cls := "description")(
              i(cls := "phone icon")(),
              text(user.phone)
            ),
            div(cls := "description")(
              i(cls := "globe icon")(),
              text(user.website)
            )
          )
        ),
        div(cls := "ui card")(
          div(cls := "content")(
            div(cls := "header")(
              i(cls := "address book icon")(),
              text("Address")
            ),
            div(cls := "description")(user.address.street),
            div(cls := "description")(user.address.suite),
            div(cls := "description")(user.address.city),
            div(cls := "description")(user.address.zipcode),
            div(cls := "description")(s"Lat: ${user.address.geo.lat}"),
            div(cls := "description")(s"Long: ${user.address.geo.lng}")
          )
        ),
        div(cls := "ui card")(
          div(cls := "content")(
            div(cls := "header")(
              i(cls := "building icon")(),
              text("Company")
            ),
            div(cls := "description")(user.company.name),
            div(cls := "description")(user.company.catchPhrase),
            div(cls := "description")(user.company.bs)
          )
        )
      ),
      div(cls := "five wide column")(
        h3(cls := "ui header")(
          div(cls := "content")(
            i(cls := "list icon")(),
            text("To-Do List")
          )
        ),
        div(cls := "ui relaxed divided list")(
          todos.map { todo =>
            div(cls := "item")(
              if todo.completed then i(cls := "check icon")()
              else i(cls := "square outline icon")(),
              div(cls := "content")(
                div(cls := "description")(todo.title)
              )
            )
          }
        )
      ),
      div(cls := "five wide column")(
        h3(cls := "ui header")(
          div(cls := "content")(
            i(cls := "edit icon")(),
            text("Posts")
          )
        ),
        div(cls := "ui relaxed divided list")(
          posts.map { post =>
            div(cls := "item")(
              i(cls := "edit icon")(),
              div(cls := "content")(
                a(cls := "header", onClick(Messages.DisplayUserPost(user, post)))(post.title),
                div(cls := "description")(text(post.body))
              )
            )
          }
        )
      )
    ) :: Nil

  def postView(post: Post, comments: List[Comment]): List[Html[Messages]] =
    div(cls := "ui card", style("width", "94%"))(
      div(cls := "content")(
        div(cls := "header")(
          i(cls := "edit icon")(),
          text(post.title)
        ),
        div(cls := "description")(text(post.body))
      )
    ) ::
      div(cls := "ui relaxed divided list", style("width", "94%"))(
        comments.map { comment =>
          div(cls := "item")(
            i(cls := "large comment top aligned icon")(),
            div(cls := "content")(
              div(cls := "header")(comment.name),
              div(cls := "meta")(
                i(cls := "envelope icon")(),
                text(comment.email)
              ),
              br,
              div(cls := "item")(p(comment.body))
            )
          )
        }
      ) :: Nil
