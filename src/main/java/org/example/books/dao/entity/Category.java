package org.example.books.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "categories")
public class Category {
  @Id
  @Column(name = "category_name")
  private String name;
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Book> books = new ArrayList<>();

  public Category(String categoryName) {
    this.name = categoryName;
  }
}
