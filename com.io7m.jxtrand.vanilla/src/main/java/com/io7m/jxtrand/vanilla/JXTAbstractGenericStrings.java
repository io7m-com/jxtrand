/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com> http://io7m.com
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

package com.io7m.jxtrand.vanilla;

import com.io7m.jxtrand.api.JXTStringConstantType;
import com.io7m.jxtrand.api.JXTStringsGenericType;
import com.io7m.jxtrand.api.JXTXMLResourceBundlesType;
import org.osgi.annotation.versioning.ProviderType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

/**
 * An abstract string provider.
 *
 * @param <T> The generic type of the string constants used
 */

@ProviderType
public abstract class JXTAbstractGenericStrings<T extends JXTStringConstantType>
  implements JXTStringsGenericType<T>
{
  private final ResourceBundle resources;

  protected JXTAbstractGenericStrings(
    final ResourceBundle inResources)
  {
    this.resources =
      Objects.requireNonNull(inResources, "inResources");
  }

  protected JXTAbstractGenericStrings(
    final Locale locale,
    final Class<?> clazz,
    final String directory,
    final String name)
  {
    this(fromClassResource(locale, clazz, directory, name));
  }

  private static ResourceBundle fromClassResource(
    final Locale locale,
    final Class<?> clazz,
    final String directory,
    final String name)
  {
    try {
      return ServiceLoader.load(JXTXMLResourceBundlesType.class)
        .findFirst()
        .orElseThrow()
        .ofResource(locale, clazz, directory, name);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final ResourceBundle resources()
  {
    return this.resources;
  }
}
