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

package org.springframework.http.server.coroutines

import org.springframework.http.CoHttpInputMessage
import org.springframework.http.HttpRequest
import org.springframework.http.server.reactive.ServerHttpRequest

interface CoServerHttpRequest : CoHttpInputMessage, HttpRequest {

	fun mutate(): Builder

	fun extractServerHttpRequest(): ServerHttpRequest

	companion object {
		operator fun invoke(request: ServerHttpRequest): CoServerHttpRequest =
				DefaultCoServerHttpRequest(request)
	}

	interface Builder {
		fun header(key: String, value: String): Builder

		fun path(path: String): Builder

		fun build(): CoServerHttpRequest
	}
}

class DefaultCoServerHttpRequestBuilder(val builder: ServerHttpRequest.Builder) :
		CoServerHttpRequest.Builder {

	override fun header(key: String, value: String): CoServerHttpRequest.Builder = apply {
		builder.header(key, value)
	}

	override fun path(path: String): CoServerHttpRequest.Builder = apply {
		builder.path(path)
	}

	override fun build(): CoServerHttpRequest = CoServerHttpRequest(builder.build())
}
