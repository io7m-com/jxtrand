/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.jxtrand.compiler.basic.internal;

import com.io7m.jodist.ClassName;
import com.io7m.jodist.JavaFile;
import com.io7m.jodist.MethodSpec;
import com.io7m.jodist.TypeSpec;
import com.io7m.jxtrand.api.JXTStringConstantType;
import com.io7m.jxtrand.compiler.api.JXTCompilerConfiguration;
import com.io7m.jxtrand.compiler.api.JXTCompilerType;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeSet;

/**
 * The default resource compiler.
 */

public final class JXTCompiler implements JXTCompilerType
{
  private final JXTCompilerConfiguration configuration;

  /**
   * The default resource compiler.
   *
   * @param inConfiguration The configuration
   */

  public JXTCompiler(
    final JXTCompilerConfiguration inConfiguration)
  {
    this.configuration =
      Objects.requireNonNull(inConfiguration, "configuration");
  }

  @Override
  public void execute()
    throws IOException
  {
    final var properties =
      this.parseProperties();
    final var file =
      this.generateClass(properties);

    this.writeFile(file);
  }

  private void writeFile(
    final JavaFile file)
    throws IOException
  {
    file.writeTo(this.configuration.directoryOutput());
  }

  private JavaFile generateClass(
    final Properties properties)
  {
    final var specBuilder = TypeSpec.enumBuilder(
      ClassName.get(
        this.configuration.packageName(),
        this.configuration.className()
      )
    );

    final var sortedNames =
      new TreeSet<>(properties.stringPropertyNames());

    specBuilder.addJavadoc("Automatically generated - DO NOT EDIT.");
    specBuilder.addModifiers(Modifier.PUBLIC);
    specBuilder.addSuperinterface(JXTStringConstantType.class);
    for (final var name : sortedNames) {
      final var enumConstantBuilder =
        TypeSpec.anonymousClassBuilder("")
          .addMethod(
            MethodSpec.methodBuilder("propertyName")
              .addAnnotation(Override.class)
              .addModifiers(Modifier.PUBLIC)
              .addStatement("return $S", name)
              .returns(String.class)
              .build());

      enumConstantBuilder.addJavadoc("Constant for resource {@code $S}.", name);
      specBuilder.addEnumConstant(
        transformPropertyNameToEnum(name),
        enumConstantBuilder.build()
      );
    }

    return JavaFile.builder(
      this.configuration.packageName(),
      specBuilder.build()
    ).build();
  }

  private static String transformPropertyNameToEnum(
    final String name)
  {
    final var codePoints =
      name.codePoints()
        .map(Character::toUpperCase)
        .map(JXTCompiler::transformJavaIdentifierCharacter)
        .boxed()
        .toList();

    final var builder = new StringBuilder(codePoints.size());
    for (final var codePoint : codePoints) {
      builder.appendCodePoint(codePoint.intValue());
    }
    return builder.toString();
  }

  private static int transformJavaIdentifierCharacter(
    final int x)
  {
    if (Character.isJavaIdentifierPart(x)) {
      return x;
    }
    return (int) '_';
  }

  private Properties parseProperties()
    throws IOException
  {
    final var properties = new Properties();

    try (var stream = Files.newInputStream(this.configuration.fileInput())) {
      properties.loadFromXML(stream);
      return properties;
    } catch (final InvalidPropertiesFormatException e) {
      // Might not be XML.
    }

    try (var stream = Files.newInputStream(this.configuration.fileInput())) {
      properties.load(stream);
      return properties;
    }
  }

  @Override
  public void close()
  {

  }
}
