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


package com.io7m.jxtrand.compiler.api;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The configuration for a compilation.
 *
 * @param fileInput       The input property file
 * @param packageName     The output package name
 * @param className       The output class name
 * @param directoryOutput The output directory
 */

public record JXTCompilerConfiguration(
  Path fileInput,
  String packageName,
  String className,
  Path directoryOutput)
{
  /**
   * The configuration for a compilation.
   *
   * @param fileInput       The input property file
   * @param packageName     The output package name
   * @param className       The output class name
   * @param directoryOutput The output directory
   */

  public JXTCompilerConfiguration
  {
    Objects.requireNonNull(fileInput, "fileInput");
    Objects.requireNonNull(packageName, "packageName");
    Objects.requireNonNull(className, "className");
    Objects.requireNonNull(directoryOutput, "directoryOutput");
  }
}