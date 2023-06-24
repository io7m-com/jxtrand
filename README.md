jxtrand
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.jxtrand/com.io7m.jxtrand.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.jxtrand%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/s01.oss.sonatype.org/com.io7m.jxtrand/com.io7m.jxtrand.svg?style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/jxtrand/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/jxtrand.svg?style=flat-square)](https://codecov.io/gh/io7m/jxtrand)

![jxtrand](./src/site/resources/jxtrand.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/jxtrand/main.linux.temurin.current.yml)](https://github.com/io7m/jxtrand/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/jxtrand/main.linux.temurin.lts.yml)](https://github.com/io7m/jxtrand/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/jxtrand/main.windows.temurin.current.yml)](https://github.com/io7m/jxtrand/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/jxtrand/main.windows.temurin.lts.yml)](https://github.com/io7m/jxtrand/actions?query=workflow%3Amain.windows.temurin.lts)|

## jxtrand

Utility classes for XML string resources, and referential integrity for
string resources.

## Motivation

Java exposes string resources that can be localized using the `ResourceBundle`
class. This can be backed by properties in a line-oriented text format. This
text format is somewhat convenient for developers, but suffers from being
line-oriented and whitespace sensitive; it becomes awkward to add significant
whitespace in strings. Java properties can also be represented in an XML-based
format that handles this better, but there is no built-in support for
loading a `ResourceBundle` from an XML file. Additionally, the actual logic
used by the `ResourceBundle` class to find property files is confusing and
complex.

Lastly, Java applications using localized strings have the property names
as string constants in the code. This means the code is free to drift out
of sync with the property files; code can refer to properties that do not
exist, and properties can become unused without much in the way of tool support
to detect unused properties.

## Usage

### XML Files

The `jxtrand` package provides a convenient abstract class to load resources
from XML property files. For example, an application could define the following
class:

```
/**
 * An example where the resource is exported.
 */

public final class ExampleStrings0 extends JXTAbstractStrings
{
  /**
   * Construct an example.
   *
   * @param locale The locale
   *
   * @throws IOException On I/O errors
   */

  public ExampleStrings0(final Locale locale)
    throws IOException
  {
    super(
      locale,
      ExampleStrings0.class,
      "/com/io7m/jxtrand/examples",
      "Messages"
    );
  }
}
```

The class takes a `Locale` as an argument, and will look in the directory
`/com/io7m/jxtrand/examples` in the module containing `ExampleStrings0.class`;
`jxtrand` is module-aware.

Given:

```
final var lang = locale.getLanguage();
final var country = locale.getCountry();
final var ex = locale.getVariant();
```

The lookup procedure will look at the following files in order:

```
 /com/io7m/jxtrand/examples/Messages_${lang}_${country}_${ex}.xml
 /com/io7m/jxtrand/examples/Messages_${lang}_${country}.xml
 /com/io7m/jxtrand/examples/Messages_${lang}.xml
 /com/io7m/jxtrand/examples/Messages.xml
```

### Modules

When using `jxtrand` in a modular context, the `jxtrand` will need to be
able to search, reflectively, for resources inside application modules.
Therefore, those modules need to be open for reflection to `jxtrand`. The
following is typically necessary in module descriptors (assuming that
resource files are kept in `/com/io7m/example/internal`:

```
module com.io7m.example
{
  opens com.io7m.example.internal
    to com.io7m.jxtrand.vanilla;

  exports com.io7m.example;
}
```

### Resource Compiler

The `jxtrand` package provides a simple resource compiler that can produce
`enum` classes from string property files (in XML or line-based formats).

Given a property file such as:

```
<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">

<properties>
  <entry key="error.01">Error 1!</entry>
  <entry key="example2">Example 2</entry>
</properties>
```

The compiler can produce an `enum` file such as:

```
public enum Strings implements JXTStringConstantType
{
  ERROR_01 {
    @Override
    public String propertyName()
    {
      return "error.01";
    }
  },

  EXAMPLE2 {
    @Override
    public String propertyName()
    {
      return "example2";
    }
  }
}
```

These constants can be used directly with instances of the `JXTStringsType`
class:


```
JXTStringsType resources;

String formatted = resources.format(ERROR_01);
```

### Maven Plugin

The `jxtrand` package publishes a Maven plugin to perform resource compilation
as part of a build. Use a plugin execution similar to the following:

```
<build>
  <plugins>
    <plugin>
      <groupId>${project.groupId}</groupId>
      <artifactId>com.io7m.jxtrand.maven_plugin</artifactId>
      <version>${project.version}</version>
      <executions>
        <execution>
          <id>generate-resources-0</id>
          <goals>
            <goal>generateSources</goal>
          </goals>
          <configuration>
            <inputFile>${project.basedir}/src/main/resources/com/io7m/jxtrand/examples/internal/Messages.xml</inputFile>
            <packageName>com.io7m.jxtrand.examples</packageName>
            <className>GeneratedStrings</className>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

In the above example, the file `src/main/resources/com/io7m/jxtrand/examples/internal/Messages.xml`
would be used to generate a Java class `com.io7m.jxtrand.examples.GeneratedStrings`. The
class is generated as a plain Java source file and is placed in
`${project.build.directory}/generated-sources/jxtrand` by default
(and the `jxtrand` directory is registered as a generated source directory
automatically).

