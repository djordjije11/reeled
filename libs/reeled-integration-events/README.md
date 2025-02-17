# Reeled Integration Events

A java library containing avro definitions of Kafka events used by services.

## Building & Testing Compatibility

### Adding new schema

In order to test compatibility and register a new schema to Schema Registry, it is required to add new schema definition
to
`schemaRegistry` configuration in `build.gradle`. Example:

```groovy
def newSchema1 = [subject: 'mySubject', schema: 'file/path.avsc', dependencies: []]
def newSchema2 = [subject: 'otherSubject', schema: 'other/path.avsc', dependencies: []]
def newSchema3 = [subject: 'subjectWithDependencies', schema: 'dependent/path.avsc', dependencies: ['firstDependency/path.avsc', 'secondDependency/path.avsc']]
```

After adding new schema definition, it is required to add it to the defined `subjects` list:

```groovy
def subjects = [
        oldSchema1,
        oldSchema2,
        oldSchema3,
        newSchema1,
        newSchema2,
        newSchema3
]
```

If you do not have dependencies with other schemas, then the third parameter should be empty array like in provided
examples from above. However, if you have dependencies with other schemas required before the compatibility check, you
can add a third parameter with the needed paths. The order of the file paths in the list is significant. Basically you
need to follow the logical order of the types used. If an `User` need an `Address` record which itself needs a `Street`
record you will need to define the dependencies like this:

```groovy
def userSchema = [subject: 'userSubject', schema: 'path/user.avsc', dependencies: ['path/address.avsc', 'path/street.avsc']]
```

### Build locally

```bash
./gradlew clean build
```

### Test Compatibility

A schema compatibility level that used in this project is `BACKWARD`, which means that compatibility is checked
against last schema version and allowed changes are only adding optional fields and removing fields.

To test compatibility, run testSchemasTask:

```bash
./gradlew testSchemasTask \
    -PschemaRegistryUrl=${SCHEMA_REGISTRY_URL} \
    -PschemaRegistryApiKey=${SCHEMA_REGISTRY_API_KEY} \
    -PschemaRegistryApiSecret=${SCHEMA_REGISTRY_API_SECRET} \
    -PreeledResourcePrefix=reeled-default-
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
    
    implementation 'io.github.djordjije11.reeled:reeled-integration-events:<version>'
}
```
