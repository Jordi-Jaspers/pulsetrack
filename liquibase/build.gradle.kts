import org.gradle.jvm.toolchain.JavaLanguageVersion.of

/**
 * The repositories used to download the dependencies.
 */
repositories {
    mavenLocal()
    mavenCentral()
}

/**
 * Project Plugins
 */
plugins {
    // Java Management
    id("java")
    id("java-library")

    // Liquibase Plugin
    id("org.liquibase.gradle") version "2.2.2"

    // Automatically generates a list of updatable dependencies.
    id("com.github.ben-manes.versions") version "0.51.0"
}

/**
 * Java 21 is long term supported and therefore chosen as the default.
 */
java {
    toolchain {
        languageVersion.set(of(21))
    }
}

/**
 * The dependencies of the project.
 */
dependencies {
    // Load the dependencies needed for liquibase to run (changelog parsers and JDBC drivers)
    liquibaseRuntime(group = "org.liquibase", name = "liquibase-core", version = "4.29.2")
    liquibaseRuntime(group = "info.picocli", name = "picocli", version = "4.7.6")
    liquibaseRuntime(group = "org.yaml", name = "snakeyaml", version = "2.3")
    liquibaseRuntime(group = "org.postgresql", name = "postgresql", version = "42.7.3")
}

/**
 * Configure the Liquibase plugin with passed properties.
 */
project.ext["env"] = System.getProperty("env")
when {
    project.ext["env"] == "custom" -> {
        project.ext["dbUrl"] = System.getProperty("dbUrl")
        project.ext["dbUsername"] = System.getProperty("dbUsername")
        project.ext["dbPassword"] = System.getProperty("dbPassword")
        project.ext["contexts"] = System.getProperty("contexts")
        project.ext["outputFile"] = System.getProperty("outputFile")
        project.ext["changelogFile"] = System.getProperty("changelogFile")
    }

    else -> {
        // No env specified: Use the configs for local development
        project.ext["dbUrl"] = "jdbc:postgresql://localhost:3306/tst_pulsetrack"
        project.ext["dbUsername"] = "tst_pulsetrack"
        project.ext["dbPassword"] = "tst_pulsetrack"
        project.ext["contexts"] = "test"
        project.ext["outputFile"] = "../build/liquibase/liquibaseChanges.sql"
        project.ext["changelogFile"] = "database/db.changelog.yaml"
    }
}

/**
 * The liquibase task.
 */
liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "output-default-catalog" to "false",
            "output-default-schema" to "false",
            "changelogFile" to project.ext["changelogFile"],
            "url" to project.ext["dbUrl"],
            "username" to project.ext["dbUsername"],
            "password" to project.ext["dbPassword"],
            "contexts" to project.ext["contexts"],
            "outputFile" to project.ext["outputFile"]
        )
    }
    runList = "main"
}
