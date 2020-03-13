import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
    mavenCentral()
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("com.google.cloud.tools.jib") version "2.1.0"
    id("com.adarshr.test-logger") version "2.0.0"
    id("org.owasp.dependencycheck") version "5.2.2"

    application
}

val http4kVersion = "3.217.0"
val spekVersion = "2.0.5"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("net.logstash.logback:logstash-logback-encoder:6.1")

    implementation("org.http4k:http4k-core:$http4kVersion")
    implementation("org.http4k:http4k-client-okhttp:$http4kVersion")
    implementation("org.http4k:http4k-format-jackson:$http4kVersion")
    implementation("org.http4k:http4k-resilience4j:$http4kVersion")
    implementation("org.http4k:http4k-server-jetty:$http4kVersion")

    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation("org.glassfish.jaxb:txw2:2.3.2")
    implementation("com.sun.xml.ws:jaxws-rt:2.3.2")

    implementation("com.natpryce:result4k:2.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.20")
    testImplementation("org.skyscreamer:jsonassert:1.5.0")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    testImplementation(gradleTestKit())
}

application {
    mainClassName = "MainKt"
}

jib {
    container {
        mainClass = application.mainClassName
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    test {
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
        testlogger {
            theme = MOCHA
        }
    }
}