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


package com.io7m.jxtrand.tests;

import com.io7m.jxtrand.compiler.api.JXTCompilerConfiguration;
import com.io7m.jxtrand.compiler.basic.JXTCompilers;
import com.sun.source.util.JavacTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class JXTCompilerTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(JXTCompilerTest.class);

  @Test
  public void testBasic(
    final @TempDir Path tempDir)
    throws Exception
  {
    final var properties = new Properties();
    properties.setProperty("a", "text0");
    properties.setProperty("b", "text1");
    properties.setProperty("c", "text2");
    properties.setProperty("com.io7m.example", "text3");

    final var propFile = tempDir.resolve("props.txt");
    try (var output = Files.newOutputStream(propFile)) {
      properties.store(output, "");
    }

    final var compilers =
      new JXTCompilers();

    final var configuration =
      new JXTCompilerConfiguration(
        propFile,
        "com.io7m.jxtrand.tests.example",
        "Strings",
        tempDir,
        Optional.empty()
      );

    try (var compiler = compilers.createCompiler(configuration)) {
      compiler.execute();
    }

    final var classFile =
      tempDir.resolve("com")
        .resolve("io7m")
        .resolve("jxtrand")
        .resolve("tests")
        .resolve("example")
        .resolve("Strings.java");

    this.compileJava(tempDir, classFile);
  }

  @Test
  public void testBasicXML(
    final @TempDir Path tempDir)
    throws Exception
  {
    final var properties = new Properties();
    properties.setProperty("a", "text0");
    properties.setProperty("b", "text1");
    properties.setProperty("c", "text2");
    properties.setProperty("com.io7m.example", "text3");

    final var propFile = tempDir.resolve("props.txt");
    try (var output = Files.newOutputStream(propFile)) {
      properties.storeToXML(output, "");
    }

    final var compilers =
      new JXTCompilers();

    final var configuration =
      new JXTCompilerConfiguration(
        propFile,
        "com.io7m.jxtrand.tests.example",
        "Strings",
        tempDir,
        Optional.empty()
      );

    try (var compiler = compilers.createCompiler(configuration)) {
      compiler.execute();
    }

    final var classFile =
      tempDir.resolve("com")
        .resolve("io7m")
        .resolve("jxtrand")
        .resolve("tests")
        .resolve("example")
        .resolve("Strings.java");

    this.compileJava(tempDir, classFile);
  }

  private void compileJava(
    final Path output,
    final Path file)
    throws IOException
  {
    LOG.debug("{}", Files.readString(file));

    final var listener =
      new Diagnostics();
    final var tool =
      ToolProvider.getSystemJavaCompiler();

    try (var fileManager = tool.getStandardFileManager(listener, ROOT, UTF_8)) {
      final var compileArguments =
        List.of(
          "-g",
          "-Werror",
          "-Xdiags:verbose",
          "-Xlint:unchecked",
          "-d",
          output.toAbsolutePath().toString()
        );

      final var task =
        (JavacTask) tool.getTask(
          null,
          fileManager,
          listener,
          compileArguments,
          null,
          List.of(new SourceFile(file))
        );

      final var result =
        task.call();

      assertTrue(
        result.booleanValue(),
        "Compilation of all files must succeed"
      );
    }
  }
}
