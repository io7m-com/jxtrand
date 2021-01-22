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

package com.io7m.jxtrand.vanilla.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The default XML resource bundle implementation.
 */

public final class JXTXMLResourceBundle extends ResourceBundle
{
  private final Properties properties;

  private JXTXMLResourceBundle(
    final Properties inProperties)
  {
    this.properties =
      Objects.requireNonNull(inProperties, "properties");
  }

  /**
   * Load a bundle from the given stream.
   *
   * @param stream The stream
   *
   * @return A resource bundle
   *
   * @throws IOException On I/O errors
   */

  public static ResourceBundle fromStream(
    final InputStream stream)
    throws IOException
  {
    final var props = new Properties();
    props.loadFromXML(Objects.requireNonNull(stream, "stream"));
    return new JXTXMLResourceBundle(props);
  }

  @Override
  protected Object handleGetObject(
    final String key)
  {
    return this.properties.getProperty(
      Objects.requireNonNull(key, "key")
    );
  }

  @Override
  public Enumeration<String> getKeys()
  {
    final Set<String> handleKeys = this.properties.stringPropertyNames();
    return Collections.enumeration(handleKeys);
  }
}
