package org.example.books.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookListResponse {
  private List<BookResponse> booksList = new ArrayList<>();
}
