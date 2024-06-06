package elementix.test

import MyRoute
import TestDTO
import elementix.trpc.installTRpcPlugin
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.reflect.*
import tRpcPluginConfiguration

fun main() {
    println(typeInfo<TestDTO>())
    println(TestDTO.serializer())

    MyRoute.repeatWordProcedure.bindHandler { input ->
        input.repeat(2) + "#Hello from procedure#"
    }
    MyRoute.test.bindHandler { input ->
        input.list.map { it * input.multiplier }
    }

    embeddedServer(Netty, port = 8080) {
        installTRpcPlugin(
            MyRoute,
            configuration = tRpcPluginConfiguration
        )
    }.start(wait = true)



}

