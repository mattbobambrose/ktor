/*
 * Copyright 2014-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.engine.js

import io.ktor.client.engine.*
import io.ktor.utils.io.*

/**
 * A JavaScript client engine that uses the fetch API to execute requests.
 *
 * To create the client with this engine, pass it to the `HttpClient` constructor:
 * ```kotlin
 * val client = HttpClient(Js)
 * ```
 * You can also call the [JsClient] function to get the [Js] engine singleton:
 * ```kotlin
 * val client = JsClient()
 * ```
 *
 * You can learn more about client engines from [Engines](https://ktor.io/docs/http-client-engines.html).
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.engine.js.Js)
 */
public actual data object Js : HttpClientEngineFactory<JsClientEngineConfig> {
    override fun create(block: JsClientEngineConfig.() -> Unit): HttpClientEngine =
        JsClientEngine(JsClientEngineConfig().apply(block))
}

/**
 * Configuration for the [Js] client.
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.engine.js.JsClientEngineConfig)
 */
public actual open class JsClientEngineConfig : HttpClientEngineConfig() {
    internal var requestInit: io.ktor.client.fetch.RequestInit.() -> Unit = {}

    /**
     * Provides access to the underlying fetch options of the engine.
     * It allows setting credentials, cache, mode, redirect, referrer, integrity, keepalive, signal, window.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.engine.js.JsClientEngineConfig.configureRequest)
     */
    public fun configureRequest(block: io.ktor.client.fetch.RequestInit.() -> Unit) {
        requestInit = block
    }

    /**
     * An `Object` which can contain additional configuration options that should get passed to node-fetch.
     *
     * For example, this can be used to configure a custom `Agent`:
     *
     * ```kotlin
     * HttpClient(Js) {
     *     engine {
     *         val agentOptions = js("Object").create(null)
     *         agentOptions.minVersion = "TLSv1.2"
     *         agentOptions.maxVersion = "TLSv1.3"
     *         nodeOptions.agent = Agent(agentOptions)
     *     }
     * }
     * ```
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.engine.js.JsClientEngineConfig.nodeOptions)
     */
    @Deprecated("Use configureRequest instead", level = DeprecationLevel.WARNING)
    public var nodeOptions: dynamic = js("Object").create(null)
}

@Suppress("DEPRECATION")
@OptIn(ExperimentalStdlibApi::class, ExperimentalJsExport::class, InternalAPI::class)
@Deprecated("", level = DeprecationLevel.HIDDEN)
@JsExport
@EagerInitialization
public val initHook: dynamic = engines.append(Js)
