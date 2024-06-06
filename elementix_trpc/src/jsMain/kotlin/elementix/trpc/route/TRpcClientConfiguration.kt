package elementix.trpc.route

import elementix.trpc.TRpcPluginConfiguration
import elementix.trpc.serialization.serializers
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

object TRpcClientConfiguration {
    lateinit var backendUrl: Url
    var pluginConfiguration: TRpcPluginConfiguration = TRpcPluginConfiguration()
    internal val client: HttpClient = HttpClient(Js) {
        install(Logging)
        install(ContentNegotiation) {
            json(json)
        }
    }
}
internal val json = Json { prettyPrint = true; serializersModule = serializers }

fun initializeClientConfig(configuration: TRpcClientConfiguration.()->Unit) {
    TRpcClientConfiguration.apply(configuration)
}
