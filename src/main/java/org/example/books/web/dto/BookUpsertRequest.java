package org.example.books.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.books.util.ErrorMsg;
import org.example.books.util.StringSizes;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpsertRequest {
  @NotBlank(message = ErrorMsg.TITLE_EMPTY)
  @Size(min = StringSizes.TITLE_MIN, max = StringSizes.TITLE_MAX, message = ErrorMsg.TITLE_LENGTH_INVALID)
  private String title;
  @NotBlank(message = ErrorMsg.AUTHOR_EMPTY)
  @Size(min = StringSizes.AUTHOR_MIN, max = StringSizes.AUTHOR_MAX, message = ErrorMsg.AUTHOR_LENGTH_INVALID)
  private String author;
  @NotBlank(message = ErrorMsg.CATEGORY_EMPTY)
  @Size(min = StringSizes.CATEGORY_MIN, max = StringSizes.CATEGORY_MAX, message = ErrorMsg.CATEGORY_LENGTH_INVALID)
  private String categoryName;
}
