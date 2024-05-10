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

package com.io7m.jxtrand.tests;

import com.io7m.jxtrand.examples.ExampleStrings0;
import com.io7m.jxtrand.examples.ExampleStrings1;
import com.io7m.jxtrand.examples.ExampleStrings2;
import com.io7m.jxtrand.examples.ExampleStrings3;
import com.io7m.jxtrand.examples.ExampleStrings4;
import com.io7m.jxtrand.examples.GeneratedRedStrings;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ExampleStringsTest
{
  @Test
  public void testExample0()
    throws IOException
  {
    final var strings = new ExampleStrings0(Locale.ENGLISH);
    assertEquals("Example 0", strings.format("example0"));
    assertNotNull(strings.resources());

    final var spliterator =
      Spliterators.spliteratorUnknownSize(
        strings.resources().getKeys().asIterator(),
        Spliterator.ORDERED
      );

    final var keys =
      StreamSupport.stream(spliterator, false)
        .collect(Collectors.toSet());

    assertEquals(1, keys.size());
    assertTrue(keys.contains("example0"));
  }

  @Test
  public void testExample1()
  {
    final var ex =
      assertThrows(
        UncheckedIOException.class,
        () -> new ExampleStrings1(Locale.ENGLISH)
      );
    assertInstanceOf(FileNotFoundException.class, ex.getCause());
  }

  @Test
  public void testExample2()
    throws IOException
  {
    final var strings = new ExampleStrings2(Locale.ENGLISH);
    assertEquals("Example 2", strings.format("example2"));
    assertNotNull(strings.resources());

    final var spliterator =
      Spliterators.spliteratorUnknownSize(
        strings.resources().getKeys().asIterator(),
        Spliterator.ORDERED
      );

    final var keys =
      StreamSupport.stream(spliterator, false)
        .collect(Collectors.toSet());

    assertEquals(1, keys.size());
    assertTrue(keys.contains("example2"));
  }

  @Test
  public void testExample3German()
    throws IOException
  {
    final var strings = new ExampleStrings3(Locale.GERMAN);
    assertEquals("rot", strings.format("Red"));
    assertNotNull(strings.resources());
  }

  @Test
  public void testExample3English()
    throws IOException
  {
    final var strings = new ExampleStrings3(Locale.ENGLISH);
    assertEquals("red", strings.format("Red"));
    assertNotNull(strings.resources());
  }

  @Test
  public void testExample3EnglishTyped()
    throws IOException
  {
    final var strings = new ExampleStrings3(Locale.ENGLISH);
    assertEquals("red", strings.format(GeneratedRedStrings.RED));
    assertNotNull(strings.resources());
  }

  @Test
  public void testExample4German()
    throws IOException
  {
    final var strings = new ExampleStrings4(Locale.GERMAN);
    assertEquals("rot", strings.format(GeneratedRedStrings.RED));
    assertNotNull(strings.resources());
  }

  @Test
  public void testExample4English()
    throws IOException
  {
    final var strings = new ExampleStrings4(Locale.ENGLISH);
    assertEquals("red", strings.format(GeneratedRedStrings.RED));
    assertNotNull(strings.resources());
  }
}
