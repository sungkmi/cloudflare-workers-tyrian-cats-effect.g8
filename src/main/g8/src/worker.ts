// Import the Scala.js export and attach it as the Cloudflare Worker fetch handler
import { handleFetch } from 'scalajs:worker.js'

export default {
  fetch: handleFetch,
}