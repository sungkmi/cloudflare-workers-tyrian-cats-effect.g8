package $package$.shared

import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

final case class UuidResponse(uuid: String)
object UuidResponse:
  given Encoder[UuidResponse] = deriveEncoder
  given Decoder[UuidResponse] = deriveDecoder

// GET /api/uuid -> { "uuid": "..." }
val uuidEndpoint: PublicEndpoint[Unit, Unit, UuidResponse, Any] =
  endpoint.get.in("api" / "uuid").out(jsonBody[UuidResponse])