package org.example.books.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "book_title")
  private String title;
  @Column(name = "author")
  private String author;
  @ManyToOne
  @JoinColumn(name = "category_name")
  @ToString.Exclude
  private Category category;

  public Book(String title, String author, Category category) {
    this.title = title;
    this.author = author;
    this.category = category;
  }
}
