# Reeled Commons

A java library containing common functionality used by other libraries and services:

* Utility/Helper classes
* Common exceptions hierarchy

## Building & Publishing

Requirements:

* JDK 21 -
  suggested [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
* [non-unix only] Git BASH or other BASH emulator
* Create environment variables permanently /$HOME/.zshrc to access maven repository from Github Packages with the following properties.

```bash
export REELED_GH_PACKAGES_USERNAME=djordjije11
export REELED_GH_PACKAGES_TOKEN=`<token>`
```

### Build locally - without tests

```bash
./gradlew clean build -x test
```

### Build locally

```bash
./gradlew clean build
```

### Publish locally

1. Update the `artifactVersion` in `gradle.properties` file.
2. Build the project.
3. Publish the artifact by running:

```bash
./gradlew publish
```

## Usage

### Gradle

```
repositories {
    ...
    
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/djordjije11/reeled")
        credentials {
            username = System.getProperty('REELED_GH_PACKAGES_USERNAME') ?: System.getenv("REELED_GH_PACKAGES_USERNAME")
            password = System.getProperty('REELED_GH_PACKAGES_READ_TOKEN') ?: System.getenv("REELED_GH_PACKAGES_READ_TOKEN")
        }
    }
}

dependencies {
    ...
    
    implementation 'io.github.djordjije11.reeled:reeled-commons:<version>'
}
```
