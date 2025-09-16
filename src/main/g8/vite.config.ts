import { defineConfig } from 'vite'
import scalaJSPlugin from '@scala-js/vite-plugin-scalajs'
import { cloudflare } from '@cloudflare/vite-plugin'

export default defineConfig({
  plugins: [
    // One instance per Scala.js sbt subproject you want Vite to watch
    scalaJSPlugin({ projectID: 'frontend' }),
    scalaJSPlugin({ projectID: 'worker' }),
    cloudflare(),
  ],
})