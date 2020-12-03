package io.github.rodrigoaddor.bingo.generator

import com.squareup.kotlinpoet.*
import kotlinx.metadata.Flag
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes("io.github.rodrigoaddor.bingo.generator.Subcommand")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(ListenerGenerator.KAPT_KOTLIN_GENERATED)
class SubcommandGenerator : AbstractProcessor() {

    companion object {
        internal const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Subcommand::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val targetType = processingEnv.elementUtils.getTypeElement(Subcommand::class.java.canonicalName).asType()
        if (annotations.isEmpty() || annotations.any {
                !processingEnv.typeUtils.isSameType(
                    it.asType(),
                    targetType
                )
            }) {
            return false
        }

        val genDir = processingEnv.options[KAPT_KOTLIN_GENERATED] ?: return false.also {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "cant find target directory for generated files")
        }

        val elements = roundEnv.getElementsAnnotatedWith(Subcommand::class.java)
        if (elements.any { it.kind != ElementKind.CLASS }) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "invalid annotation kind (non-class)")
            return false
        }

        val registerCode = CodeBlock
            .builder()
            .addStatement(
                "%T(name)",
                ClassName("dev.jorel.commandapi", "CommandAPICommand")
            )
            .indent()

        elements.forEach { e ->
            e as TypeElement
            val metadata = e.getAnnotation(Metadata::class.java).run {
                KotlinClassMetadata.read(KotlinClassHeader(
                    kind,
                    metadataVersion,
                    bytecodeVersion,
                    data1,
                    data2,
                    extraString,
                    packageName,
                    extraInt
                ))
            }
            val isObject = metadata is KotlinClassMetadata.Class && Flag.Class.IS_OBJECT(metadata.toKmClass().flags)
            val str = if (isObject) {
                ".withSubcommand(%T)"
            } else {
                ".withSubcommand(%T())"
            }
            val pkg = processingEnv.elementUtils.getPackageOf(e).toString()
            registerCode.addStatement(str, ClassName(pkg, e.simpleName.toString()))
        }

        registerCode
            .add(".register()")
            .unindent()

        val objSpec = TypeSpec
            .objectBuilder("RegisterSubcommands")
            .addFunction(
                FunSpec
                    .builder("invoke")
                    .addModifiers(KModifier.OPERATOR)
                    .addParameter("name", STRING)
                    .addCode(registerCode.build())
                    .build()
            )
            .build()

        FileSpec.builder("io.github.rodrigoaddor.bingo.generated", objSpec.name!!)
            .addType(objSpec)
            .build()
            .writeTo(File(genDir))

        return true
    }

}