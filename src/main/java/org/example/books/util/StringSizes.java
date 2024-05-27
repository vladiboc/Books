/**
 * Здесь собраны все константы - размеры строк для валидации
 */
package org.example.books.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringSizes {
  public static final int TITLE_MIN = 2;
  public static final int TITLE_MAX = 128;
  public static final int AUTHOR_MIN = 2;
  public static final int AUTHOR_MAX = 64;
  public static final int CATEGORY_MIN = 2;
  public static final int CATEGORY_MAX = 32;
}
