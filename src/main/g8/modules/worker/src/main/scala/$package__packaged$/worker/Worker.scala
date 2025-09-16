package $package$.worker

import cats.effect.*
import cats.effect.unsafe.implicits.global
import cats.syntax.all.*
import scala.scalajs.js
import scala.scalajs.js.annotation.*
import org.scalajs.dom
import scala.util.Random

import $package$.shared.*

@JSExportTopLevel("handleFetch")
def handleFetch(request: dom.Request, env: js.Any, ctx: js.Any): js.Promise[dom.Response] =
  handleRequest[IO](request).unsafeToPromise()

def handleRequest[F[_]: Async](request: dom.Request): F[dom.Response] =
  val url = new dom.URL(request.url)
  routeRequest[F](request.method.toString, url.pathname).map { case (statusCode, body, contentType) =>
    val init = new dom.ResponseInit {
      status = statusCode
      headers = js.Array(js.Array("Content-Type", contentType))
    }
    new dom.Response(body, init)
  }

def routeRequest[F[_]: Async](method: String, path: String): F[(Int, String, String)] =
  (method, path) match
    case ("GET", "/api/uuid") =>
      generateUuid[F].map { uuid =>
        val json = js.JSON.stringify(js.Dynamic.literal("uuid" -> uuid))
        (200, json, "application/json")
      }
    case _ =>
      Async[F].pure((404, "Not Found", "text/plain"))

def generateUuid[F[_]: Async]: F[String] =
  Async[F].delay {
    // Naive UUID v4 generator good enough for demo purposes
    val hex = "0123456789abcdef"
    def block(n: Int): String =
      (1 to n).map(_ => hex.charAt(Random.nextInt(16))).mkString
    val variant = "89ab"(Random.nextInt(4))
    s"\${block(8)}-\${block(4)}-4\${block(3)}-\${variant}\${block(3)}-\${block(12)}"
  }