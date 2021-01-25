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

package com.io7m.jxtrand.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The type of providers of XML-based string resource bundles.
 */

public interface JXTXMLResourceBundlesType
{
  /**
   * Load a resource bundle from the input stream.
   *
   * @param stream The stream
   *
   * @return A resource bundle
   *
   * @throws IOException On I/O errors
   */

  ResourceBundle ofInputStream(InputStream stream)
    throws IOException;

  /**
   * Load a resource bundle from the named class resource. The class
   * {@code clazz} will be asked to load a resource at a name derived from the
   * given {@code directory}, {@code name}, and the current default locale.
   *
   * @param clazz     The class
   * @param directory The directory
   * @param name      The resource name
   *
   * @return A resource bundle
   *
   * @throws IOException On I/O errors
   * @see #ofResource(Locale, Class, String, String) for the lookup procedure
   */

  default ResourceBundle ofResource(
    final Class<?> clazz,
    final String directory,
    final String name)
    throws IOException
  {
    return this.ofResource(Locale.getDefault(), clazz, directory, name);
  }

  /**
   * Load a resource bundle from the named class resource. The class
   * {@code clazz} will be asked to load a resource at a name derived from the
   * given {@code directory}, {@code name}, and {@code locale}.
   *
   * Given:
   *
   * <pre>
   *   final var lang = locale.getLanguage();
   *   final var country = locale.getCountry();
   *   final var ex = locale.getVariant();
   * </pre>
   *
   * The lookup procedure will look at the following files in order:
   *
   * <pre>
   * /directory/name_lang_country_ex.xml
   * /directory/name_lang_country.xml
   * /directory/name_lang.xml
   * /directory/name.xml
   * </pre>
   *
   * @param locale    The locale
   * @param clazz     The class
   * @param directory The directory
   * @param name      The resource name
   *
   * @return A resource bundle
   *
   * @throws IOException On I/O errors
   */

  ResourceBundle ofResource(
    Locale locale,
    Class<?> clazz,
    String directory,
    String name)
    throws IOException;
}
