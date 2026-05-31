package com.example.personallibrary.data;

public class Book {
    private int id;
    private String title;
    private String author;
    private String description;
    private String liked;
    private String disliked;
    private String imagePath;

    public Book(int id, String title, String author, String description, String liked, String disliked, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getDisliked() {
        return disliked;
    }

    public void setDisliked(String disliked) {
        this.disliked = disliked;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}