import org.gradle.kotlin.dsl.support.expectedKotlinDslPluginsVersion

plugins {
    `kotlin-dsl`
}

group = "org.postgresql.build-logic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // We use precompiled script plugins (== plugins written as src/kotlin/build-logic.*.gradle.kts files,
    // and we need to declare dependency on org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin
    // to make it work.
    // See https://github.com/gradle/gradle/issues/17016 regarding expectedKotlinDslPluginsVersion
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:$expectedKotlinDslPluginsVersion")
}

kotlinDslPluginOptions {
    jvmTarget.set("1.8")
}
