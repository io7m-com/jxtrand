/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jxtrand.maven_plugin;

import com.io7m.jxtrand.compiler.api.JXTCompilerConfiguration;
import com.io7m.jxtrand.compiler.basic.JXTCompilers;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;
import java.util.Optional;

/**
 * The "generate sources" mojo.
 */

@Mojo(
  name = "generateSources",
  defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public final class JXTMojo extends AbstractMojo
{
  /**
   * The input file.
   */

  @Parameter(
    name = "inputFile",
    required = true)
  private String inputFile;

  /**
   * The output Java package name.
   */

  @Parameter(
    name = "packageName",
    required = true)
  private String packageName;

  /**
   * The output Java class name.
   */

  @Parameter(
    name = "className",
    required = true)
  private String className;

  /**
   * The extra superinterface implemented by string constants.
   */

  @Parameter(
    name = "extraSuperInterface",
    required = false)
  private String extraSuperInterface;

  /**
   * The output directory.
   */

  @Parameter(
    name = "outputDirectory",
    defaultValue = "${project.build.directory}/generated-sources/jxtrand",
    required = false)
  private String outputDirectory;

  /**
   * The Maven project.
   */

  @Parameter(readonly = true, defaultValue = "${project}")
  private MavenProject project;

  /**
   * Instantiate the mojo.
   */

  public JXTMojo()
  {

  }

  @Override
  public void execute()
    throws MojoExecutionException
  {
    try {
      final var compilers =
        new JXTCompilers();
      final var outputPath =
        Paths.get(this.outputDirectory);

      final var configuration =
        new JXTCompilerConfiguration(
          Paths.get(this.inputFile),
          this.packageName,
          this.className,
          outputPath,
          Optional.ofNullable(this.extraSuperInterface)
        );

      try (var compiler = compilers.createCompiler(configuration)) {
        compiler.execute();
      }

      this.project.addCompileSourceRoot(outputPath.toString());
    } catch (final Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }
}
