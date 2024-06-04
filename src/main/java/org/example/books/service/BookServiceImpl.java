package org.example.books.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.books.aop.Loggable;
import org.example.books.config.properties.AppCacheProperties;
import org.example.books.dao.entity.Book;
import org.example.books.dao.entity.Category;
import org.example.books.dao.repository.BookRepository;
import org.example.books.dao.repository.CategoryRepository;
import org.example.books.util.ErrorMsg;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Главный (и единственный) сервис с кэшированием результатов в Redis
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;

  /**
   * Найти все книги заданной котегории
   * и закэшировать результат с ключом - названием категории
   * @param categoryName - название категории
   * @return - список книг заданной категории
   */
  @Override
  @Loggable
  @Cacheable(cacheNames = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#categoryName")
  public List<Book> findAllByCategoryName(String categoryName) {
    return this.bookRepository.findAllByCategoryName(categoryName);
  }

  /**
   * Найти книгу по названию и автору
   * и закэшировать результат с ключом название+автор
   * @param title - название
   * @param author - автор
   * @return - найденная книга
   */
  @Override
  @Loggable
  @Cacheable(
      cacheNames = AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR, key = "#title + #author")
  public Book findByTitleAndAuthor(String title, String author) {
    return this.bookRepository.findByTitleAndAuthor(title, author)
        .orElseThrow(() -> new EntityNotFoundException(ErrorMsg.BOOK_NOT_FOUND));
  }

  @Override
  @Loggable
  @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#book.category.name")
  public Book create(Book book) {
    Category category = this.findOrNew(book.getCategory().getName());
    Book newBook = new Book(book.getTitle(), book.getAuthor(), category);
    return this.bookRepository.save(newBook);
  }

  @Override
  @Loggable
  @Caching(evict = {
      @CacheEvict(
          value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY,
          key = "#root.target.findByIdBookOrNull(#id).category.name",
          condition = "#root.target.findByIdBookOrNull(#id) != null",
          beforeInvocation = true),
      @CacheEvict(
          value = AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR,
          key = "#root.target.findByIdBookOrNull(#id).title " +
              "+ #root.target.findByIdBookOrNull(#id).author",
          condition = "#root.target.findByIdBookOrNull(#id) != null",
          beforeInvocation = true)
  })
  public Book update(Book book, int id) {
    final Category category = this.findOrNew(book.getCategory().getName());
    Book updatingBook = this.findById(id);
    updatingBook.setTitle(book.getTitle());
    updatingBook.setAuthor(book.getAuthor());
    updatingBook.setCategory(category);
    return this.bookRepository.save(updatingBook);
  }

  @Override
  @Loggable
  @Caching(evict = {
      @CacheEvict(
          value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY,
          key = "#root.target.findByIdBookOrNull(#id).category.name",
          condition = "#root.target.findByIdBookOrNull(#id) != null",
          beforeInvocation = true),
      @CacheEvict(
          value = AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR,
          key = "#root.target.findByIdBookOrNull(#id).title " +
              "+ #root.target.findByIdBookOrNull(#id).author",
          condition = "#root.target.findByIdBookOrNull(#id) != null",
          beforeInvocation = true)
  })
  public void delete(int id) {
    this.bookRepository.deleteById(id);
  }

  @Loggable
  public Book findByIdBookOrNull(int id) {
    return this.bookRepository.findById(id).orElse(null);
  }

  @Loggable
  private Book findById(int id) {
    return this.bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        MessageFormat.format(ErrorMsg.BOOK_BY_ID_NOT_FOUND, id))
    );
  }

  @Loggable
  private Category findOrNew(String categoryName) {
    Category category = this.categoryRepository.findById(categoryName).orElse(null);
    if (category == null) {
      category = this.categoryRepository.save(new Category(categoryName));
    }
    return category;
  }
}
