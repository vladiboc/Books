package org.example.books.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
  private int id;
  private String title;
  private String author;
  private String categoryName;
}
