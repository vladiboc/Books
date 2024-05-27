package org.example.books.service;

import org.example.books.dao.entity.Book;

import java.util.List;

public interface BookService {
  List<Book> findAllByCategoryName(String categoryName);

  Book findByTitleAndAuthor(String title, String author);

  Book create(Book book);

  Book update(Book book, int id);

  void delete(int id);
}
