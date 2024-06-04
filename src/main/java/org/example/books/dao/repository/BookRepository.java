package org.example.books.dao.repository;

import org.example.books.dao.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория сущностей "книга"
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
  /**
   * Автогенерируемый метод поиска списка книг по наименованию категории
   * @param categoryName - непустая строка наименования категории, валидируется контроллером
   * @return - список книг, соответствующих заданной категории
   */
  List<Book> findAllByCategoryName(String categoryName);

  Optional<Book> findByTitleAndAuthor(String title, String author);
}
