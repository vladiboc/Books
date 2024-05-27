package org.example.books.web.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.books.dao.entity.Book;
import org.example.books.dao.entity.Category;
import org.example.books.mapper.BookMapper;
import org.example.books.service.BookService;
import org.example.books.util.TestStringUtil;
import org.example.books.web.dto.BookListResponse;
import org.example.books.web.dto.BookResponse;
import org.example.books.web.dto.BookUpsertRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @MockBean
  private BookService bookService;
  @MockBean
  private BookMapper bookMapper;

  @Test
  void whenFindAllByCategory_thenReturnAllBooksByCategory() throws Exception {
    final Category category = new Category("category");

    final List<Book> books = new ArrayList<>();
    books.add(new Book(1, "title1", "author1", category));
    books.add(new Book(2, "title2", "author2", category));

    final List<BookResponse> bookResponses = new ArrayList<>();
    bookResponses.add(new BookResponse(1, "title1", "author1", "category"));
    bookResponses.add(new BookResponse(2, "title2", "author2", "category"));

    final BookListResponse bookListResponse = new BookListResponse(bookResponses);

    Mockito.when(this.bookService.findAllByCategoryName("category")).thenReturn(books);
    Mockito.when(this.bookMapper.bookListToBookListResponse(books)).thenReturn(bookListResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/find_all_books_by_category.json");
    final String actualResponse = this.mockMvc.perform(get("/api/v1/book/category"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.bookService, Mockito.times(1)).findAllByCategoryName("category");
    Mockito.verify(this.bookMapper, Mockito.times(1)).bookListToBookListResponse(books);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenFindByTitleAndAuthor_thenReturnBookByTitleAndAuthor() throws Exception {
    final Category category = new Category("category");
    final Book book = new Book(1, "title", "author", category);
    final BookResponse response = new BookResponse(1, "title", "author", "category");

    Mockito.when(this.bookService.findByTitleAndAuthor("title", "author")).thenReturn(book);
    Mockito.when(this.bookMapper.bookToBookResponse(book)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/find_book_by_title_and_author_response.json");
    final String actualResponse = this.mockMvc.perform(get("/api/v1/book/title/author"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.bookService, Mockito.times(1)).findByTitleAndAuthor("title", "author");
    Mockito.verify(this.bookMapper, Mockito.times(1)).bookToBookResponse(book);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreate_thenReturnNewBook() throws Exception {
    final BookUpsertRequest request = new BookUpsertRequest("title", "author", "category");

    final Category category = new Category("category");
    final Book newBook = new Book("title", "author", category);
    final Book createdBook = new Book(1, "title", "author", category);

    final BookResponse response = new BookResponse(1, "title", "author", "category");

    Mockito.when(this.bookMapper.requestToBook(request)).thenReturn(newBook);
    Mockito.when(this.bookService.create(newBook)).thenReturn(createdBook);
    Mockito.when(this.bookMapper.bookToBookResponse(createdBook)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/create_book_response.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.bookMapper, Mockito.times(1)).requestToBook(request);
    Mockito.verify(this.bookService, Mockito.times(1)).create(newBook);
    Mockito.verify(this.bookMapper, Mockito.times(1)).bookToBookResponse(createdBook);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenUpdate_thenReturnUpdatedBook() throws Exception {
    final BookUpsertRequest request = new BookUpsertRequest("title", "author", "new category");

    final Category category = new Category("new category");
    final Book editedBook = new Book("title", "author", category);
    final Book updatedBook = new Book(1, "title", "author", category);

    final BookResponse response = new BookResponse(1, "title", "author", "new category");

    Mockito.when(this.bookMapper.requestToBook(request)).thenReturn(editedBook);
    Mockito.when(this.bookService.update(editedBook, 1)).thenReturn(updatedBook);
    Mockito.when(this.bookMapper.bookToBookResponse(updatedBook)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/update_book_response.json");
    final String actualResponse = this.mockMvc.perform(put("/api/v1/book/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.bookMapper, Mockito.times(1)).requestToBook(request);
    Mockito.verify(this.bookService, Mockito.times(1)).update(editedBook, 1);
    Mockito.verify(this.bookMapper, Mockito.times(1)).bookToBookResponse(updatedBook);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenDeleteById_thenReturnNoContent() throws Exception {
    this.mockMvc.perform(delete("/api/v1/book/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(this.bookService, Mockito.times(1)).delete(1);
  }

  @Test
  void whenFindByTitleAndAuthor_thenReturnError() throws Exception {
    Mockito.when(this.bookService.findByTitleAndAuthor("Марк", "Твен"))
        .thenThrow(new EntityNotFoundException("Книга по названию и автору не найдена!"));

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/_err_book_not_found_response.json");
    final String actualResponse = this.mockMvc.perform(get("/api/v1/book/Марк/Твен"))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.bookService, Mockito.times(1))
        .findByTitleAndAuthor("Марк", "Твен");

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }
}
