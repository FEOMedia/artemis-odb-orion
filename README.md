[![Join the odb chat at https://gitter.im/junkdog/artemis-odb](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/junkdog/artemis-odb?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## artemis-odb-orion

An Operations/Actions mini-DSL for [artemis-odb](https://github.com/junkdog/artemis-odb)
entities, built on top of [libgdx](https://github.com/libgdx/libgdx). Inspired by similar
Actions API:s, especially that of libgdx.

Provides a core collection of operations, and a simple-to-use framework for building
custom, project-specific operations.

Code ends up looking something like:
```java
Entity e = ...
sequence(
    parallel(
        sequence(
            moveTo(xy(x, y)), // project-specific
            moveBy(xy(0, type.size)), // project-specific
            moveBy(xy(0, -type.size), seconds(.6f), bounceOut)
        ),
        sequence(
            sizeTo(xy(-1, 0)), // project-specific
            delay(seconds(.2f)),
            sizeTo(xy(-1, type.size), seconds(.4f), swing)
        )
    ),
    sendEvent(POPUP_ANIMATION_DONE) // project-specific
).register(e);
```

## Features
- Actions for entities, tied into artemis-odb's native lifecycle.
- Custom operations are easy to write.
- GC friendly: no unnecessary allocations, automatic pooling.
- Full serialization support, save/load mid-operation using the
  [artemis-json-libgdx](https://github.com/junkdog/artemis-odb/wiki/libgdx-json) backend.
  - Serialized operations can also act as "attached scripts", automatically triggered on entity creation.

## Usage

No published artifacts yet - until released, install from source before referencing in project:

```
mvn clean install
```

#### Maven
```xml
<dependency>
	<groupId>se.feomedia.orion</groupId>
	<artifactId>artemis-odb-orion</artifactId>
	<version>0.1.0-SNAPSHOT</version>
</dependency>
```

See [weave automation](https://github.com/junkdog/artemis-odb/wiki/Weave-Automation) and [module overview](https://github.com/junkdog/artemis-odb/wiki/Module-Overview)

#### Gradle
```groovy
  allprojects {
    repositories {
      maven { url "https://jitpack.io" }    
    }
  }

  dependencies { compile "com.github.FEOMedia:artemis-odb-orion:-SNAPSHOT" }
```

### License

Artemis-odb-orion is licensed under the Apache 2 License, meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.
