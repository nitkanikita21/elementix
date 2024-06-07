package elementix.trpc.route

import elementix.trpc.serialization.ResultSerializer
import io.konform.validation.Validation
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.js.Promise
import kotlin.properties.Delegates

actual class Procedure<I : Any?, R : Any?> {
    actual var route: Route by Delegates.notNull()
    actual var name: String by Delegates.notNull()
    actual var validator: Validation<I>? = null

    var inputTypeInfo: TypeInfo by Delegates.notNull()
    var returnTypeInfo: TypeInfo by Delegates.notNull()

    @OptIn(DelicateCoroutinesApi::class)
    fun call(input: I): Promise<Result<R>> =
        GlobalScope.promise {
            println("res1")

            val response = TRpcClientConfiguration.client.post(
                URLBuilder(TRpcClientConfiguration.backendUrl).apply {
                    path(TRpcClientConfiguration.pluginConfiguration.endpoint, route.name, name)
                }.build()
            ) {
                this.contentType(ContentType.Application.Json)
                this.header(HttpHeaders.AccessControlAllowOrigin, "*")
                this.setBody(
                    json.encodeToString(
                        serializer(inputTypeInfo.kotlinType!!),
                        input
                    )
                )
            }

            println("res2")
            json.decodeFromString(ResultSerializer(serializer(returnTypeInfo.kotlinType!!) as KSerializer<R>), response.bodyAsText()).also {
                println("res3")
            }

        }
}

actual inline fun <reified I : Any?, reified R : Any?> Procedure(
    route: Route,
    name: String,
    validator: Validation<I>?
): Procedure<I, R> {
    return Procedure<I, R>().apply {
        this.name = name
        this.validator = validator
        this.route = route
        this.inputTypeInfo = typeInfo<I>()
        this.returnTypeInfo = typeInfo<R>()
    }
}