# Cloudflare Workers Tyrian Cats-Effect g8 Template

A [Giter8][g8] template for scaffolding a full-stack Scala.js application with:
- **Frontend**: Tyrian + Cats Effect + Tapir client
- **Backend**: Cloudflare Workers (Scala.js)
- **Build**: Vite + sbt

## Usage

```bash
sbt new sungkmi/cloudflare-workers-tyrian-cats-effect.g8
```

Follow the prompts to configure your project, then:

```bash
cd <project-name>
yarn
sbt ";frontend/fastLinkJS;worker/fastLinkJS"
yarn dev
```

## Features

- Type-safe API definitions using Tapir
- Functional reactive frontend with Tyrian
- Cloudflare Workers backend in Scala.js
- Hot module replacement with Vite
- Ready-to-deploy configuration

## Template License

Written in 2025 by sungkmi

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <https://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: https://www.foundweekends.org/giter8/
