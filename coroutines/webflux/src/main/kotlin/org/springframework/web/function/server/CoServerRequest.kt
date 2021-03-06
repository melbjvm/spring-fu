/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.function.server

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.openSubscription
import org.springframework.http.server.coroutines.CoServerHttpRequest
import org.springframework.web.function.CoBodyExtractor
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.CoWebSession
import org.springframework.web.server.session.asCoroutines
import java.net.URI

interface CoServerRequest {
	fun <T> body(extractor: CoBodyExtractor<T, CoServerHttpRequest>): T

	fun <T> body(extractor: CoBodyExtractor<T, CoServerHttpRequest>, hints: Map<String, Any>): T

	suspend fun <T> body(elementClass: Class<out T>): T?

	fun <T> bodyToReceiveChannel(elementClass: Class<out T>): ReceiveChannel<T>

	fun headers(): ServerRequest.Headers

	fun pathVariable(name: String): String?

	suspend fun session(): CoWebSession?

	fun uri(): URI

	fun extractServerRequest(): ServerRequest

	companion object {
		operator fun invoke(req: ServerRequest) = DefaultCoServerRequest(req)
	}
}

class DefaultCoServerRequest(private val req: ServerRequest) : CoServerRequest {
	override fun <T> body(extractor: CoBodyExtractor<T, CoServerHttpRequest>): T =
		req.body(extractor.asBodyExtractor())

	override fun <T> body(
			extractor: CoBodyExtractor<T, CoServerHttpRequest>,
			hints: Map<String, Any>
	): T =
		req.body(extractor.asBodyExtractor(), hints)

	override suspend fun <T> body(elementClass: Class<out T>): T? =
		req.bodyToMono(elementClass).awaitFirstOrDefault(null)

	override fun <T> bodyToReceiveChannel(elementClass: Class<out T>): ReceiveChannel<T> =
		req.bodyToFlux(elementClass).openSubscription()

	override fun headers(): ServerRequest.Headers = req.headers()

	override fun pathVariable(name: String): String? = req.pathVariable(name)

	override suspend fun session(): CoWebSession? =
		req.session().awaitFirstOrDefault(null)?.asCoroutines()

	override fun uri(): URI = req.uri()

	override fun extractServerRequest(): ServerRequest = req
}

suspend inline fun <reified T : Any> CoServerRequest.body(): T? = body(T::class.java)


