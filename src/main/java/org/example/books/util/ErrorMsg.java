package org.example.books.util;

import lombok.experimental.UtilityClass;

/**
 * Класс содержит всевозможные строки - сообщения об ошибках
 */
@UtilityClass
public class ErrorMsg {
  public static final String NO_RESOURCE_FOUND = "Ресурс не найден!";
  public static final String INTERNAL_SERVER_ERROR = "Внутренняя ошибка сервера:  : {}";
  public static final String CATEGORY_EMPTY = "Название категории должно быть задано!";
  public static final String CATEGORY_LENGTH_INVALID = "Размер наименования категории должен быть от {min} до {max} символов!";
  public static final String TITLE_EMPTY = "Название книги должно быть задано!";
  public static final String TITLE_LENGTH_INVALID = "Размер названия книги должен быть от {min} до {max} символов!";
  public static final String AUTHOR_EMPTY = "Имя автора должно быть задано!";
  public static final String AUTHOR_LENGTH_INVALID = "Размер имени автора должен быть от {min} до {max} символов!";
  public static final String ID_NEGATIVE = "Идентификатор должен быть положительным числом!";
  public static final String BOOK_NOT_FOUND = "Книга по названию и автору не найдена!";
  public static final String BOOK_BY_ID_NOT_FOUND = "Книга с id {} не найдена!";
}
