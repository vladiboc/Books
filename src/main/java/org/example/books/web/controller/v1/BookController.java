package org.example.books.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.books.aop.Loggable;
import org.example.books.dao.entity.Book;
import org.example.books.mapper.BookMapper;
import org.example.books.service.BookService;
import org.example.books.util.ErrorMsg;
import org.example.books.util.StringSizes;
import org.example.books.web.dto.BookListResponse;
import org.example.books.web.dto.BookResponse;
import org.example.books.web.dto.BookUpsertRequest;
import org.example.books.web.dto.ErrorMsgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Tag(name = "Книги 1.0", description = "Управление книгами 1.0")
public class BookController {
  private final BookService bookService;
  private final BookMapper bookMapper;

  @Operation(
      summary = "Получить список книг заданной категории.",
      description = "Возвращает список книг с номерами, названиями, автором, категориями.")
  @Parameter(name = "categoryName", required = true, in = ParameterIn.PATH, description = "Категория книг.")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = BookListResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping("/{categoryName}")
  public ResponseEntity<BookListResponse> findAllByCategory(
      @PathVariable @NotBlank(message = ErrorMsg.CATEGORY_EMPTY)
      @Size(min = StringSizes.CATEGORY_MIN, max = StringSizes.CATEGORY_MAX, message = ErrorMsg.CATEGORY_LENGTH_INVALID)
      String categoryName) {
    List<Book> books = this.bookService.findAllByCategoryName(categoryName);
    BookListResponse response = this.bookMapper.bookListToBookListResponse(books);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить книгу по названию и автору",
      description = "Возвращает номер книги, название, автора, категорию.")
  @Parameter(name = "title", required = true, in = ParameterIn.PATH, description = "Название книги.")
  @Parameter(name = "author", required = true, in = ParameterIn.PATH, description = "Автор книги.")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = BookResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping("/{title}/{author}")
  public ResponseEntity<BookResponse> findByTitleAndAuthor(
      @PathVariable
        @NotBlank(message = ErrorMsg.TITLE_EMPTY)
        @Size(min = StringSizes.TITLE_MIN, max = StringSizes.TITLE_MAX, message = ErrorMsg.TITLE_LENGTH_INVALID)
        String title,
      @PathVariable
        @NotBlank(message = ErrorMsg.AUTHOR_EMPTY)
        @Size(min = StringSizes.AUTHOR_MIN, max = StringSizes.TITLE_MAX, message = ErrorMsg.AUTHOR_LENGTH_INVALID)
        String author
  ) {
    Book book = this.bookService.findByTitleAndAuthor(title, author);
    BookResponse response = this.bookMapper.bookToBookResponse(book);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать книгу.",
      description = "Возвращает номер созданной книги, название, автора, категорию.")
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = BookResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @PostMapping
  public ResponseEntity<BookResponse> create(@RequestBody @Valid BookUpsertRequest request) {
    Book newBook = this.bookMapper.requestToBook(request);
    Book createdBook = this.bookService.create(newBook);
    BookResponse response = this.bookMapper.bookToBookResponse(createdBook);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить книгу.",
      description = "Возвращает номер обновленной книги, название, автора, категорию.")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = BookResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @PutMapping("/{id}")
  public ResponseEntity<BookResponse> update(
      @PathVariable @Positive(message = ErrorMsg.ID_NEGATIVE) int id,
      @RequestBody @Valid BookUpsertRequest request
  ) {
    Book editedBook = this.bookMapper.requestToBook(request);
    Book updatedBook = this.bookService.update(editedBook, id);
    BookResponse response = this.bookMapper.bookToBookResponse(updatedBook);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Удалить книгу по номеру.", description = "Удаляет книгу по номеру.")
  @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Идентификатор книги.")
  @ApiResponse(responseCode = "204")
  @Loggable
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @Positive(message = ErrorMsg.ID_NEGATIVE) int id) {
    this.bookService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
