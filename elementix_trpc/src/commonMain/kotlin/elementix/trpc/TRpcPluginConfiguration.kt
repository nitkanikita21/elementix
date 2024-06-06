package elementix.trpc

import io.ktor.http.*

data class TRpcPluginConfiguration(
    val endpoint: String = "/trpc"
)