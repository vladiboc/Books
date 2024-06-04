package org.example.books.service;

import org.example.books.dao.entity.Book;
import org.example.books.dao.entity.Category;
import org.example.books.dao.repository.BookRepository;
import org.example.books.dao.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
  private BookRepository bookRepository = Mockito.mock(BookRepository.class);
  private CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  private final BookService bookService = new BookServiceImpl(this.bookRepository, this.categoryRepository);

  @Test
  void whenFindByTitleAndAuthor_thenReturnBookByTitleAndAuthor() {
    final Category category = new Category("Детская литература");
    final Book expectedBook = new Book(1, "Приключения Тома Сойера", "Марк Твен", category);

    Mockito.when(this.bookRepository.findByTitleAndAuthor("Приключения Тома Сойера", "Марк Твен"))
        .thenReturn(Optional.of(expectedBook));

    Book actualBook = this.bookService.findByTitleAndAuthor("Приключения Тома Сойера", "Марк Твен");

    Mockito.verify(this.bookRepository, Mockito.times(1))
        .findByTitleAndAuthor("Приключения Тома Сойера", "Марк Твен");

    assertEquals(expectedBook, actualBook);
  }

  @Test
  void whenFindAllByCategoryName_thenReturnAllBooksByCategory() {
    final Category category = new Category("Детская литература");
    final List<Book> expectedBookList = new ArrayList<>();

    Mockito.when(this.bookRepository.findAllByCategoryName(category.getName())).thenReturn(new ArrayList<>());

    final List<Book> actualBookList = this.bookService.findAllByCategoryName(category.getName());

    Mockito.verify(this.bookRepository, Mockito.times(1)).findAllByCategoryName(category.getName());

    assertEquals(expectedBookList, actualBookList);
  }

  @Test
  void whenUpdate_thenReturnUpdatedBook() {
    final Category category = new Category("Детская литература");
    final Book existedBook = new Book(1, "Приключения Тома Сойера", "Марк Твен", category);
    final Book editedBook = new Book("Приключения Гекльберри Финна", "Марк Твен", category);
    final Book expectedBook = new Book(1, "Приключения Гекльберри Финна", "Марк Твен", category);

    Mockito.when(this.categoryRepository.findById("Детская литература")).thenReturn(Optional.of(category));
    Mockito.when(this.bookRepository.findById(1)).thenReturn(Optional.of(existedBook));
    Mockito.when(this.bookRepository.save(expectedBook)).thenReturn(expectedBook);

    final Book actualBook = this.bookService.update(editedBook, 1);

    Mockito.verify(this.categoryRepository, Mockito.times(1)).findById(category.getName());
    Mockito.verify(this.bookRepository, Mockito.times(1)).findById(1);
    Mockito.verify(this.bookRepository, Mockito.times(1)).save(expectedBook);

    assertEquals(expectedBook, actualBook);
  }
}
