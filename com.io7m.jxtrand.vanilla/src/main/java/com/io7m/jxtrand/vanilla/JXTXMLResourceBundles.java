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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
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
    final Class<?> clazz,
    final String name)
    throws IOException
  {
    Objects.requireNonNull(clazz, "clazz");
    Objects.requireNonNull(name, "name");

    final var url = clazz.getResource(name);
    if (url == null) {
      throw new NoSuchFileException(name);
    }

    try (var stream = url.openStream()) {
      return this.ofInputStream(stream);
    }
  }
}
