package org.springframework.boot.kofu.samples

import org.springframework.boot.kofu.application
import org.springframework.boot.kofu.ref
import org.springframework.boot.kofu.web.server
import org.springframework.web.function.server.coRouter
import org.springframework.web.reactive.function.server.router

private fun router() {
	fun routes(htmlHandler: HtmlHandler, apiHandler: ApiHandler) = router {
		GET("/", htmlHandler::blog)
		GET("/article/{id}", htmlHandler::article)
		"/api".nest {
			GET("/", apiHandler::list)
			POST("/", apiHandler::create)
			PUT("/{id}", apiHandler::update)
			DELETE("/{id}", apiHandler::delete)
		}
	}
	application {
		server {
			router(::routes)
		}
	}
}

private fun coRouter() {
	fun routes(htmlHandler: HtmlCoroutinesHandler, apiHandler: ApiCoroutinesHandler) = coRouter {
		GET("/", htmlHandler::blog)
		GET("/article/{id}", htmlHandler::article)
		"/api".nest {
			GET("/", apiHandler::list)
			POST("/", apiHandler::create)
			PUT("/{id}", apiHandler::update)
			DELETE("/{id}", apiHandler::delete)
		}
	}
	application {
		beans {
			bean<HtmlCoroutinesHandler>()
			bean<ApiCoroutinesHandler>()
		}
		server {
			router(::routes)
		}
	}
}
