package org.example.books.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {
  private final List<String> cacheNames = new ArrayList<>();
  private final Map<String, CacheProperties> caches = new HashMap<>();

  @Data
  public static class CacheProperties {
    private Duration expiry = Duration.ZERO;
  }

  public interface CacheNames {
    String BOOKS_BY_CATEGORY = "booksByCategory";
    String BOOK_BY_TITLE_AND_AUTHOR = "bookByTitleAndAuthor";
  }
}
