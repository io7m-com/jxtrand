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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ExampleStrings0Test
{
  @Test
  public void testOpen()
    throws IOException
  {
    final var strings = new ExampleStrings0();
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
  public void testOpenNonexistent()
  {
    assertThrows(NoSuchFileException.class, ExampleStrings1::new);
  }
}
