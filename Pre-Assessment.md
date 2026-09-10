# Postman SDK Team Pre-Interview Guide

Good news, everyone! Planet Express moves cargo between planets for customers, stakeholders, and a fairly demanding regulatory body, and all of that generates a lot of data that needs to live somewhere and be reachable by a service. Before we get together, we'd like you to get the foundation in place.

## What to do

1. Stand up a relational database, running locally on your machine. Any engine is fine, Postgres, MySQL, SQLite, whatever you're comfortable with.
2. Stand up a backend service, in whatever language or framework you prefer, that connects to that database.
3. Confirm the two are actually talking to each other. A health check endpoint, a simple query run through the service, anything that proves the connection is real.

Pick a language and framework you already know well. This isn't the place to try something new, the live session is about shipping features against real data and tradeoffs, not fighting an unfamiliar stack. Whatever gets you moving fastest with the least friction is the right choice.

## Container requirement

Both the database and the backend service need to run in a container. Docker is the obvious choice, but any OCI-compliant runtime works too (Podman, containerd, etc.). Bring a `docker-compose.yml` (or equivalent) that brings both up with a single command. We want to be able to run your environment exactly as you built it, not just take your word for it.

## Boilerplate is fair game now

This is also the right time to wire up whatever scaffolding you'd normally want on a real project, an ORM or query builder, a logging setup, config/env handling, migrations, whatever you'd reach for before writing a single line of business logic. None of that is specific to Planet Express data, so get it in place now rather than during the live session, where we'd rather see you spend time on the actual problem instead of picking a logging library.

## What we're not asking for yet

No client-facing app, no UI, no CLI, no SDK. That part happens live, during the session. Don't get ahead of it.

## What to bring

- A repo (or a link to one) with your database and service setup.
- The compose file or equivalent that starts everything.
- Short instructions for how to verify the connection yourself, a command to run, an endpoint to hit, whatever proves it.

There's no fixed time limit on this part. It's meant to be lightweight, proving the connection works is the goal, not building anything elaborate. If you find yourself deep in schema design or business logic before we've even given you the data, that's a sign to pull back.

See you soon.
