package $package$.frontend

import tyrian.*
import tyrian.Html.*
import cats.effect.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import sttp.client3.*
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.model.Uri

import io.circe.parser.decode
import $package$.shared.*

object Main extends TyrianIOApp[Msg, Model]:
  def router: Location => Msg = Routing.none(Msg.NoOp)

  // --- STTP backend (Scala.js Fetch backend) ---
  private lazy val backend: SttpBackend[Future, Any] = FetchBackend()
  private val base: Uri = uri"/" // relative base; Vite/Worker will serve same origin

  private val makeRequest =
    SttpClientInterpreter().toRequestThrowErrors(uuidEndpoint, Some(base))

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model(Nil, busy = false, None), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.FetchUuid =>
      if model.busy then (model, Cmd.None)
      else
        val eff: IO[Msg] = IO.fromFuture(IO(makeRequest(()).send(backend))).attempt.map {
          case Right(resp) => Msg.AppendUuid(resp.body.uuid)
          case Left(err)   => Msg.Fail(err.getMessage)
        }
        (model.copy(busy = true), Cmd.Run(eff))

    case Msg.AppendUuid(u) =>
      (model.copy(uuids = u :: model.uuids, busy = false, error = None), Cmd.None)

    case Msg.Fail(e) =>
      (model.copy(busy = false, error = Some(e)), Cmd.None)

    case Msg.NoOp => (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div(
      h2("Tyrian + Tapir (client) on Cloudflare Workers"),
      div(cls := "row")(
        button(disabled(model.busy), onClick(Msg.FetchUuid))(if model.busy then "Loading…" else "Get UUID"),
        model.error.fold(span())(msg => span(cls := "error")(s"Error: \$msg"))
      ),
      ul(
        model.uuids.map(u => li(code(u)))
      )
    )

  def subscriptions(model: Model): Sub[IO, Msg] = Sub.None

final case class Model(uuids: List[String], busy: Boolean, error: Option[String])

enum Msg:
  case FetchUuid
  case AppendUuid(value: String)
  case Fail(msg: String)
  case NoOp

// Provide a module initializer so Vite can run it via `import 'scalajs:frontend.js'`
@main def runApp(): Unit = Main.launch("app")