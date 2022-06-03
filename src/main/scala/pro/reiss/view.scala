package pro.reiss

import tyrian.Html.*
import tyrian.*

def userHeader(model: Users): Html[Msg] =
  h1(`class` := "ui header")(
    i(`class` := "circular users icon")(),
    model.users match
      case user :: Nil => text(model.users.head.name)
      case _           => text("Users")
  )

def userListView(users: Users): List[Html[Msg]] =
  users.users.map { user =>
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

def userDetailView(user: User): Html[Msg] =
  div(`class` := "ui grid")(
    div(`class` := "seven wide column")(
      button(`class` := "ui left labeled icon button", onClick(Msg.GetAllUsers))(
        i(`class` := "left angle icon")(),
        text("Back")
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(text("Contact")),
          div(`class` := "description")(user.email),
          div(`class` := "description")(user.phone),
          div(`class` := "description")(user.website)
        )
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(text("Address")),
          div(`class` := "description")(user.address.street),
          div(`class` := "description")(user.address.suite),
          div(`class` := "description")(user.address.city),
          div(`class` := "description")(user.address.zipcode)
        )
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(text("Position")),
          div(`class` := "description")(s"Lat: ${user.address.geo.lat}"),
          div(`class` := "description")(s"Long: ${user.address.geo.lng}")
        )
      ),
      div(`class` := "ui card")(
        div(`class` := "content")(
          div(`class` := "header")(text("Company")),
          div(`class` := "description")(user.company.name),
          div(`class` := "description")(user.company.catchPhrase),
          div(`class` := "description")(user.company.bs)
        )
      )
    )
  )
