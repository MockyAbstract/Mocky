# Mocky

#### Mock your HTTP responses to test your REST API

Mocky is a simple app which allows to generate custom HTTP responses. <br />
It's helpful when you have to request a build-in-progress WS, when you want to mock the backend response in a singleapp,
or when you want to test your WS client.

## Is Mocky online?

Yes, Mocky is online, free and unlimited!

**Try me now! https://www.mocky.io**

*Everything is done to provide the best service quality, but I don't guarantee the sustainability or the stability of the application.*

## Mocky on my own server?

You can easily install Mocky on your own server.
You just need... a JVM!

Quick steps to install Mocky
* Install [SBT](http://www.scala-sbt.org/).
* Clone Mocky
* Configure the app in `conf/application.conf`. The main setting is the `version` key. You have 3 different repositories where you can store your mocks:
 * v1: mocks are stored on gist. Nothing is needed, it's free, but limited to 200 requests / hours.
 * v2: mocks are stored in mongodb. Don't forget to fill the `mongodb.uri` key.
 * fs: mocks are stored on your filesystem (in the `data` dir. by default).
* If you want to launch mocky in dev mode, just launch `sbt run`.
* If you want to use mocky on a server, launch `sbt dist`, copy the generated zip to your server, unzip it and launch `./bin/mocky` (easy, ain't it?)

## License

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
Version 2, December 2004

Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>

Everyone is permitted to copy and distribute verbatim or modified
copies of this license document, and changing it is allowed as long
as the name is changed.

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

0. You just DO WHAT THE FUCK YOU WANT TO.
