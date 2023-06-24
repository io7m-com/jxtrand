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

package com.io7m.jxtrand.api;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Formatting functions where the string resource ID is a generic constant.
 *
 * @param <T> The generic type of the string constants used
 */

public interface JXTStringsGenericType<T extends JXTStringConstantType>
  extends JXTStringsHasResourcesType
{
  /**
   * Format a message.
   *
   * @param id   The string resource ID
   * @param args Any required string format arguments
   *
   * @return A formatted string
   *
   * @see MessageFormat
   */

  default String format(
    final T id,
    final Object... args)
  {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(args, "args");
    return MessageFormat.format(
      this.resources().getString(id.propertyName()),
      args
    );
  }
}
