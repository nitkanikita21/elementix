package elementix.trpc.serialization

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal val serializers = SerializersModule {
    contextual(Result::class) { list ->
        ResultSerializer(list[0])
    }
    contextual(Throwable::class, ThrowableSerializer)
}