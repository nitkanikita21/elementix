package elementix.trpc

import elementix.trpc.route.Procedure
import elementix.trpc.route.Route
import elementix.trpc.serialization.ResultSerializer
import elementix.trpc.serialization.serializers
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

fun Application.installTRpcPlugin(
    vararg routes: Route,
    configuration: TRpcPluginConfiguration = TRpcPluginConfiguration()
) {
    val routeMap: Map<String, Route> = routes.associateBy { it.name }
    val json = Json { prettyPrint = true; serializersModule = serializers }

    install(ContentNegotiation) {
        json(json)
    }
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
    }


    routing {
        post("${configuration.endpoint}/{route}/{procedure}") {
            log.info("Procedure call")

            val route = routeMap[call.parameters["route"]]!!
            val s = call.parameters["procedure"]
            val procedure: Procedure<in Any, out Any> = route.procedures[s]!!
            val input = json.decodeFromString(serializer(procedure.inputTypeInfo.kotlinType!!), call.receiveText())!!
            val res = procedure.getResponse(input)


            res.map {
                val value = it.await()
                call.respondText(
                    json.encodeToString(
                        ResultSerializer(serializer(procedure.returnTypeInfo.kotlinType!!)),
                        Result.success(value)
                    ).also { log.info(it) },
                    contentType = ContentType.Application.Json
                )
            }
            if (res.isFailure) {
                call.respondText(
                    json.encodeToString(
                        ResultSerializer(serializer(procedure.returnTypeInfo.kotlinType!!)),
                        Result.failure(res.exceptionOrNull()!!)
                    ).also { log.info(it) },
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
}
