package com.osg.project01bookdiary;

public class SharedItem {
    String profileImage;
    String profileName;
    String bookCover;
    String bookTitle;
    String bookAuthor;
    String reviewTitle;
    String reviewContent;

    public SharedItem() {
    }

    public SharedItem(String profileImage, String profileName, String bookCover, String bookTitle, String bookAuthor, String reviewTitle, String reviewContent) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
    }
}
