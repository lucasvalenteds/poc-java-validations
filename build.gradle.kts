import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
}

subprojects {
    apply<JavaPlugin>()

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.2")
        testImplementation("org.assertj", "assertj-core", "3.23.1")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("--enable-preview")
    }

    tasks.test {
        useJUnitPlatform()
        jvmArgs("--enable-preview")
        testLogging {
            events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED
            )
        }
    }
}
