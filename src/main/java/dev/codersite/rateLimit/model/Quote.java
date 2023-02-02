package dev.codersite.rateLimit.model;

public class Quote {
  String author;
  String message;

  public Quote() {}

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
