import elementix.trpc.TRpcPluginConfiguration
import elementix.trpc.builder.procedure
import elementix.trpc.route.Route
import io.konform.validation.Validation
import io.konform.validation.jsonschema.*
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

val tRpcPluginConfiguration = TRpcPluginConfiguration(
    endpoint = "/my_trpc_endpoint"
)

@Serializable
data class TestDTO(
    val multiplier: Int,
    val list: List<Int>
)

object MyRoute: Route() {
    val repeatWordProcedure by procedure<String, String>("repeat", Validation {
        minLength(3)
    })
    val test by procedure<TestDTO, List<Int>>(Validation {
        TestDTO::list {
            minItems(2)
            maxItems(20)
        }
        TestDTO::multiplier {
            minimum(0)
            maximum(100)
        }
    })
}