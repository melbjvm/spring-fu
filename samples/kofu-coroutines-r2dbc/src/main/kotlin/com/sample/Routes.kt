package com.sample

import org.springframework.web.function.server.coRouter

fun routes(userHandler: UserHandler) = coRouter {
	GET("/", userHandler::listView)
	GET("/api/user", userHandler::listApi)
	GET("/conf", userHandler::conf)
}
