plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
    testImplementation("io.mockk:mockk:1.12.4")
}

sourceSets {
    main {
        output.setResourcesDir("build/classes/main")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}