# Mocky.io Lockdown Edition (2020)

![Release CI](https://github.com/julien-lafont/Mocky/workflows/Release%20CI/badge.svg)
![Development CI](https://github.com/julien-lafont/Mocky/workflows/Development%20CI/badge.svg?branch=develop)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/julien-lafont/Mocky)

## What is mocky?

Mocky is a simple app which allows to generate custom HTTP responses. It's helpful when you have to request a build-in-progress WS, when you want to mock the backend response in a single app, or when you want to test your WS client.

Don't wait for the backend to be ready, generate custom API responses with Mocky and start working on your application straight away

Mocky is a **free** and **unlimited** online service, accessible on **https://www.mocky.io**. Read the [FAQ](https://designer.mocky.io/faq) for more information about allowed usage.

_Everything is done to provide the best service quality, but I don't guarantee the sustainability or the stability of the application._

## How to host my own Mocky instance

Work in progress. Please come back in a few days!

## Architecture

### API

Mocky API is written in Scala with the [HTTP4s](https://http4s.org/) server. Mocks are stored into PostgreSQL database with [Doobie](https://tpolecat.github.io/doobie/).

See the [server/README.md](https://github.com/julien-lafont/Mocky/blob/master/server/README.md) for more information about how to build/run the API (WIP).

### Frontend

React/Redux application written in typescript.

See the [client/README.md](https://github.com/julien-lafont/Mocky/blob/master/client/README.md) for more information about how to build/run the frontend.

### Hosting

Mocky is currently hosted on [Clever-Cloud](https://www.clever-cloud.com/en/).

> Clever Cloud helps companies and IT professionals to achieve software delivery faster, reduce their feedback loop, focus on their core value and stop worrying about their hosting infrastructure by providing a solution for application sustainability.

## Contributors

- [Julien lafont](https://twitter.com/julien_lafont)

## License

Mocky is licensed under the A[pache License, Version 2.0](https://github.com/julien-lafont/Mocky/blob/master/LICENSE) (the “License”); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
