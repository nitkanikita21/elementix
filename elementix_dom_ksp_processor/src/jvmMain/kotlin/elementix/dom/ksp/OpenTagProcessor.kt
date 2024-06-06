package elementix.dom.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class OpenTagProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    fun String.toCamelCase(): String = this.replaceFirstChar { it.lowercase() }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GenerateTag::class.qualifiedName!!)
        val invalidSymbols = symbols.filter { !it.validate() }.toList()

        symbols.filterIsInstance<KSClassDeclaration>()
            .forEach { ksClassDeclaration ->
                generateTagExtension(ksClassDeclaration)
            }

        return invalidSymbols
    }

    @OptIn(KspExperimental::class)
    private fun generateTagExtension(classDeclaration: KSClassDeclaration) {
        val annotation = classDeclaration.getAnnotationsByType(GenerateTag::class).firstOrNull() ?: return

        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        val tagType = when (annotation.type) {
            TagType.CLOSED -> "Tag"
            TagType.OPENED -> "OpenedTag"
        }
        val tagName = if(annotation.name == "NOT_SPECIFIED") classDeclaration.simpleName.getShortName().toCamelCase() else annotation.name
        val rootContainer= annotation.root

        val fileSpec = FileSpec.builder(packageName, "${className}Extensions")
            .addProperty(
                PropertySpec.builder(
                    tagName, ClassName(packageName, tagType)
                        .parameterizedBy(ClassName(packageName, className))
                )
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return ${tagType}(this) { %T() }", ClassName(packageName, className))
                            .build()
                    )
                    .receiver(ClassName.bestGuess(rootContainer))
                    .build()
            )
            .build()

        val file = codeGenerator.createNewFile(
            Dependencies.ALL_FILES,
            packageName,
            "${className}Extensions"
        )

        file.bufferedWriter().use { writer ->
            fileSpec.writeTo(writer)
        }
    }
}