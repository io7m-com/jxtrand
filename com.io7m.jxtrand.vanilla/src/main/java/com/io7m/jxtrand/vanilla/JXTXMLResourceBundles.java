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

import com.io7m.jxtrand.api.JXTXMLResourceBundlesType;
import com.io7m.jxtrand.vanilla.internal.JXTXMLResourceBundle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The default provider of XML resource bundles.
 */

public final class JXTXMLResourceBundles implements JXTXMLResourceBundlesType
{
  /**
   * Construct a provider.
   */

  public JXTXMLResourceBundles()
  {

  }

  @Override
  public ResourceBundle ofInputStream(
    final InputStream stream)
    throws IOException
  {
    return JXTXMLResourceBundle.fromStream(stream);
  }

  @Override
  public ResourceBundle ofResource(
    final Locale locale,
    final Class<?> clazz,
    final String base,
    final String name)
    throws IOException
  {
    Objects.requireNonNull(locale, "locale");
    Objects.requireNonNull(clazz, "clazz");
    Objects.requireNonNull(base, "directory");
    Objects.requireNonNull(name, "name");

    final var lang = locale.getLanguage();
    final var country = locale.getCountry();
    final var ex = locale.getVariant();

    final var possibleNames =
      List.of(
        String.format("%s/%s_%s_%s_%s.xml", base, name, lang, country, ex),
        String.format("%s/%s_%s_%s.xml", base, name, lang, country),
        String.format("%s/%s_%s.xml", base, name, lang),
        String.format("%s/%s.xml", base, name)
      );

    for (final var possibleName : possibleNames) {
      final var url = clazz.getResource(possibleName);
      if (url == null) {
        continue;
      }
      try (var stream = url.openStream()) {
        return this.ofInputStream(stream);
      }
    }

    final var message = new StringBuilder(128);
    message.append("Could not locate an appropriate resource file.");
    message.append(System.lineSeparator());
    message.append("  Locale: ");
    message.append(locale);
    message.append(System.lineSeparator());
    message.append("  Class: ");
    message.append(clazz);
    message.append(System.lineSeparator());
    message.append("  Base: ");
    message.append(base);
    message.append(System.lineSeparator());
    message.append("  Name: ");
    message.append(name);
    message.append(System.lineSeparator());
    message.append("  Tried resources: ");
    message.append(System.lineSeparator());

    for (final var possibleName : possibleNames) {
      message.append("    ");
      message.append(possibleName);
      message.append(System.lineSeparator());
    }

    throw new FileNotFoundException(message.toString());
  }
}
