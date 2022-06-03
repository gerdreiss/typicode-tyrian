package pro.reiss

import tyrian.Html.*
import tyrian.*

def usersView(model: Users): Html[Msg] =
  div(`class` := "ui raised very padded container segment")(
    targetedHeader(model) :: div(`class` := "ui divider")() :: targetedView(model)
  )

def targetedHeader(model: Users): Html[Msg] =
  h1(`class` := "ui header")(
    i(`class` := "circular users icon")(),
    model.displayTarget match
      case DisplayTarget.USER =>
        userHeader(model.users.head.name, Msg.GetAllUsers)
      case DisplayTarget.POST =>
        userHeader(model.users.head.name, Msg.GetUser(model.users.head.id))
      case _ => div(`class` := "content")(text("Users"))
  )

def userHeader(t: String, msg: Msg): Html[Msg] =
  div(`class` := "content", style("width", "92%"))(
    div(`class` := "ui grid")(
      div(`class` := "row")(
        div(`class` := "fourteen wide column")(
          p(t)
        ),
        div(`class` := "two wide column")(
          button(`class` := "ui labeled button", onClick(msg))(
            i(`class` := "left arrow icon")(),
            text("Back")
          )
        )
      )
    )
  )

def targetedView(model: Users): List[Html[Msg]] =
  model.displayTarget match
    case DisplayTarget.USERS => userListView(model.users)
    case DisplayTarget.USER  => userDetailView(model.users.head, model.todos, model.posts)
    case DisplayTarget.POST  => postView(model.posts.head, model.comments)
    case DisplayTarget.ERROR => div(`class` := "content")(text(model.error.get)) :: Nil

def userListView(users: List[User]): List[Html[Msg]] =
  users.map { user =>
    div(`class` := "ui grid")(
      div(`class` := "four wide column")(
        div(`class` := "ui card")(
          div(`class` := "content")(
            div(`class` := "header")(a(onClick(Msg.GetUser(user.id)))(user.name)),
            div(`class` := "description")(
              i(`class` := "envelope icon")(),
              text(user.email)
            ),
            div(`class` := "description")(
              i(`class` := "phone icon")(),
              text(user.phone)
            ),
            div(`class` := "description")(
              i(`class` := "globe icon")(),
              text(user.website)
            ),
            br
          )
        )
      ),
      div(`class` := "three wide column")(
        div(`class` := "ui card")(
          div(`class` := "content")(
            div(`class` := "header")(
              i(`class` := "address book icon")(),
              text("Address")
            ),
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
            div(`class` := "header")(
              i(`class` := "location arrow icon")(),
              text("Position")
            ),
            div(`class` := "description")(s"Lat: ${user.address.geo.lat}"),
            div(`class` := "description")(s"Lng: ${user.address.geo.lng}"),
            br,
            br
          )
        )
      ),
      div(`class` := "six wide column")(
        div(`class` := "ui card")(
          div(`class` := "content")(
            div(`class` := "header")(
              i(`class` := "building icon")(),
              text("Company")
            ),
            div(`class` := "description")(user.company.name),
            div(`class` := "description")(user.company.catchPhrase),
            div(`class` := "extra content")(p(user.company.bs)),
            br
          )
        )
      )
    )
  }

def userDetailView(user: User, todos: List[Todo], posts: List[Post]): List[Html[Msg]] =
  div(`class` := "ui grid")(
    div(`class` := "five wide column")(
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "description")(
            i(`class` := "envelope icon")(),
            text(user.email)
          ),
          div(`class` := "description")(
            i(`class` := "phone icon")(),
            text(user.phone)
          ),
          div(`class` := "description")(
            i(`class` := "globe icon")(),
            text(user.website)
          )
        )
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(
            i(`class` := "address book icon")(),
            text("Address")
          ),
          div(`class` := "description")(user.address.street),
          div(`class` := "description")(user.address.suite),
          div(`class` := "description")(user.address.city),
          div(`class` := "description")(user.address.zipcode),
          div(`class` := "description")(s"Lat: ${user.address.geo.lat}"),
          div(`class` := "description")(s"Long: ${user.address.geo.lng}")
        )
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(
            i(`class` := "building icon")(),
            text("Company")
          ),
          div(`class` := "description")(user.company.name),
          div(`class` := "description")(user.company.catchPhrase),
          div(`class` := "description")(user.company.bs)
        )
      )
    ),
    div(`class` := "five wide column")(
      h3(`class` := "ui header")(
        div(`class` := "content")(
          i(`class` := "list icon")(),
          text("To-Do List")
        )
      ),
      div(`class` := "ui relaxed divided list")(
        todos.map { todo =>
          div(`class` := "item")(
            if todo.completed then i(`class` := "check icon")()
            else i(`class` := "square outline icon")(),
            div(`class` := "content")(
              div(`class` := "description")(todo.title)
            )
          )
        }
      )
    ),
    div(`class` := "five wide column")(
      h3(`class` := "ui header")(
        div(`class` := "content")(
          i(`class` := "edit icon")(),
          text("Posts")
        )
      ),
      div(`class` := "ui relaxed divided list")(
        posts.map { post =>
          div(`class` := "item")(
            i(`class` := "check icon")(),
            div(`class` := "content")(
              a(`class` := "header", onClick(Msg.DisplayPost(user, post)))(post.title),
              div(`class` := "description")(text(post.body))
            )
          )
        }
      )
    )
  ) :: Nil

def postView(post: Post, comments: List[Comment]): List[Html[Msg]] =
  div(`class` := "ui card", style("width", "94%"))(
    div(`class` := "content")(
      div(`class` := "header")(
        i(`class` := "edit icon")(),
        text(post.title)
      ),
      div(`class` := "description")(text(post.body))
    )
  ) ::
    div(`class` := "ui divider")() ::
    comments.map { comment =>
      div(`class` := "ui relaxed divided list", style("width", "94%"))(
        div(`class` := "item")(
          i(`class` := "large comment middle aligned icon")(),
          div(`class` := "content")(
            div(`class` := "header")(comment.name),
            div(`class` := "meta")(
              i(`class` := "envelope icon")(),
              text(comment.email)
            ),
            br,
            div(`class` := "item")(p(comment.body)),
            div(`class` := "ui divider")()
          )
        )
      )
    }
