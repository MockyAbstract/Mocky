package io.mocky.models

import io.mocky.http.middleware.Role

case class Gate[T <: Role](role: T) extends AnyVal
