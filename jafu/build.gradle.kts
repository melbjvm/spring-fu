plugins {
	id("io.spring.dependency-management")
	id("java-library")
}

dependencies {
	api("org.springframework.boot:spring-boot")
	implementation(project(":autoconfigure-adapter"))
	compileOnly("org.springframework:spring-webflux")
}

publishing {
	publications {
		create(project.name, MavenPublication::class.java) {
			from(components["java"])
			artifactId = "spring-fu-jafu"
			val sourcesJar by tasks.creating(Jar::class) {
				classifier = "sources"
				from(sourceSets["main"].allSource)
			}
			artifact(sourcesJar)
			val javadocJar by tasks.creating(Jar::class) {
				dependsOn("javadoc")
				classifier = "javadoc"
				from(buildDir.resolve("dokka"))
			}
			artifact(javadocJar)
		}
	}
}