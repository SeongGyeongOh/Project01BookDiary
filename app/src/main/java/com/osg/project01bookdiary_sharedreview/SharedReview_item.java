package com.osg.project01bookdiary_sharedreview;

public class SharedReview_item {
    int no;
    String profileImage, profileName, bookCover, bookTitle, bookAuthor, reviewTitle, reviewContent, date;

    public SharedReview_item(int no, String profileImage, String profileName, String bookCover, String bookTitle, String bookAuthor, String reviewTitle, String reviewContent, String date) {
        this.no = no;
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.date = date;
    }
}
