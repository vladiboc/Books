package org.example.books.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.books.aop.Loggable;
import org.example.books.dao.entity.Book;
import org.example.books.dao.entity.Category;
import org.example.books.dao.repository.BookRepository;
import org.example.books.dao.repository.CategoryRepository;
import org.example.books.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;

  @Override
  @Loggable
  public List<Book> findAllByCategoryName(String categoryName) {
    return this.bookRepository.findAllByCategoryName(categoryName);
  }

  @Override
  @Loggable
  public Book findByTitleAndAuthor(String title, String author) {
    return this.bookRepository.findByTitleAndAuthor(title, author);
  }

  @Override
  @Loggable
  public Book create(Book book) {
    Category category = this.findOrNew(book.getCategory().getName());
    Book newBook = new Book(book.getTitle(), book.getAuthor(), category);
    return this.bookRepository.save(newBook);
  }

  @Override
  @Loggable
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
  public void delete(int id) {
    this.bookRepository.deleteById(id);
  }

  private Category findOrNew(String categoryName) {
    Category category = this.categoryRepository.findById(categoryName).orElse(null);
    if (category == null) {
      category = this.categoryRepository.save(new Category(categoryName));
    }
    return category;
  }

  private Book findById(int id) {
    return this.bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(ErrorMsg.BOOK_BY_ID_NOT_FOUND, id)
            )
        );
  }
}
