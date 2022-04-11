group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
  java
  jacoco
  application
}

repositories {
  mavenCentral()
}

application {
  mainClassName = "main.PokemonInit"
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation("org.mockito:mockito-core:4.2.0")
  implementation("org.json:json:20211205")
  implementation("org.apache.commons:commons-text:1.9")
}

//dependencies {
//  testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
//  testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
//  testRuntime("org.junit.platform:junit-platform-console:1.2.0")
//  testImplementation("org.hamcrest:hamcrest:2.1")
//  compile(group = "org.slf4j", name = "slf4j-jdk14", version = "1.7.25")
//  compile(group = "com.fasterxml.jackson.core", name = "jackson-databind", version="2.10.0.pr3")
//}

sourceSets {
  main {
    java.srcDirs("PokemonBattle/src")
  }
  test {
    java.srcDirs("PokemonBattle/tests")
  }
}

tasks {
  val treatWarningsAsError =
    listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

  getByName<JavaCompile>("compileJava") {
    options.compilerArgs = treatWarningsAsError
  }

  getByName<JavaCompile>("compileTestJava") {
    options.compilerArgs = treatWarningsAsError
  }

  getByName<JacocoReport>("jacocoTestReport") {
    afterEvaluate {
      classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) { exclude("**/main/**") }
      }))
    }
  }
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform {}
}

defaultTasks("clean", "test", "jacocoTestReport")