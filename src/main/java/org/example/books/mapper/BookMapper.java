package org.example.books.mapper;

import org.example.books.dao.entity.Book;
import org.example.books.dao.entity.Category;
import org.example.books.web.dto.BookListResponse;
import org.example.books.web.dto.BookResponse;
import org.example.books.web.dto.BookUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
  default Book requestToBook(BookUpsertRequest request) {
    return new Book(
        request.getTitle(),
        request.getAuthor(),
        new Category(request.getCategoryName())
    );
  }

  default BookResponse bookToBookResponse(Book book) {
    return new BookResponse(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getCategory().getName()
    );
  }

  List<BookResponse> bookListToBookResponseList(List<Book> books);

  default BookListResponse bookListToBookListResponse(List<Book> books) {
    return new BookListResponse(this.bookListToBookResponseList(books));
  }
}
