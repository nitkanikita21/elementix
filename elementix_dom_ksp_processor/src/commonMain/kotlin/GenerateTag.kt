package elementix.dom.ksp

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateTag(
    val root: String,
    val name: String = "NOT_SPECIFIED",
    val type: TagType = TagType.OPENED
)
