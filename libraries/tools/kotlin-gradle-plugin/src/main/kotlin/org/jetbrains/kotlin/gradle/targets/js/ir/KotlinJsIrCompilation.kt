/*
* Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
* Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
*/

package org.jetbrains.kotlin.gradle.targets.js.ir

import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.BuildVariantKind
import org.jetbrains.kotlin.gradle.targets.js.dsl.BuildVariantKind.DEVELOPMENT
import org.jetbrains.kotlin.gradle.targets.js.dsl.BuildVariantKind.PRODUCTION
import org.jetbrains.kotlin.gradle.utils.lowerCamelCaseName

class KotlinJsIrCompilation(
    target: KotlinTarget,
    name: String
) : KotlinJsCompilation(target, name) {
    val productionLinkTaskName: String = linkTaskName(PRODUCTION)

    val productionLinkTask: TaskProvider<KotlinJsIrLink>
        get() = target.project.tasks.named(productionLinkTaskName) as TaskProvider<KotlinJsIrLink>

    val developmentLinkTaskName: String = linkTaskName(DEVELOPMENT)

    val developmentLinkTask: TaskProvider<KotlinJsIrLink>
        get() = target.project.tasks.named(developmentLinkTaskName) as TaskProvider<KotlinJsIrLink>

    private fun linkTaskName(type: BuildVariantKind): String =
        lowerCamelCaseName(
            "compile",
            type.name.toLowerCase(),
            compilationName.takeIf { it != KotlinCompilation.MAIN_COMPILATION_NAME },
            "Kotlin",
            target.targetName
        )

    internal val allSources: MutableSet<SourceDirectorySet> = mutableSetOf()

    override fun addSourcesToCompileTask(sourceSet: KotlinSourceSet, addAsCommonSources: Lazy<Boolean>) {
        super.addSourcesToCompileTask(sourceSet, addAsCommonSources)
        allSources.add(sourceSet.kotlin)
    }

    override val defaultSourceSetName: String
        get() {
            return lowerCamelCaseName(
                if ((target as KotlinJsIrTarget).mixedMode)
                    target.disambiguationClassifier
                        ?.removeSuffix(IR_TARGET_SUFFIX)
                else
                    target.disambiguationClassifier,
                compilationName
            )
        }
}