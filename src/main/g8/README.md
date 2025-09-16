# $name$

Full-stack Scala.js (Tyrian + Cats Effect) + Tapir client + Vite + Cloudflare Workers.

## Prerequisites
- Yarn (Classic) and Node.js
- sbt
- Cloudflare account + `wrangler` CLI

## Install dependencies
```bash
yarn
```

## First build (Scala.js)

```bash
# Build once so Vite has JS modules to load
sbt ";frontend/fastLinkJS;worker/fastLinkJS"
```

## Dev mode

Open two terminals:

```bash
# 1) Vite (serves index.html + HMR + Workers dev)
yarn dev

# 2) Incremental linking from sbt (frontend)
sbt "~frontend/fastLinkJS"

# (optional) 3) Another terminal for the worker if you edit it often
sbt "~worker/fastLinkJS"
```

Click **Get UUID** to call the Tapir-typed endpoint; each result is appended to the list.

## Production build

```bash
yarn build
```

## Preview & deploy

```bash
yarn preview
yarn deploy
```

### Notes

* The Tapir endpoint definition lives in `shared` and is consumed by the frontend using the **Tapir sttp client interpreter** with the **Scala.js Fetch backend**. The Worker implements the route without Tapir to keep it tiny.
* Everything is emitted as **ES modules**; the Worker entry (`src/worker.ts`) imports `handleFetch` from Scala.js and exposes it as `export default { fetch }`.