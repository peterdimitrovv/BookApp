package com.example.booksapp;

public class BookModel {
    private int id;
    private String name;
    private String genre;
    private String author;
    private int year;
    private double rating;
    private String description;
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public BookModel(int id, String name, String genre, String author, int year, double rating, String description, byte[] image) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }
}
