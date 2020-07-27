package com.osg.project01bookdiary;

import android.widget.RatingBar;

public class Tab01myreview_item {

    int no;
    String image;
    String bookTitle;
    String bookAuthor;
    String reviewTitle;
    String reviewContent;

    String date;
//    int rating;

    public Tab01myreview_item(int no, String image, String bookTitle, String bookAuthor, String reviewTitle, String reviewContent, String date) {
        this.no = no;
        this.image = image;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.date = date;
//        this.rating=rating;
    }
}
