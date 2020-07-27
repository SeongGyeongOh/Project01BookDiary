package com.osg.project01bookdiary;

import java.util.ArrayList;

public class VOBookItem {

    Meta meta;
    ArrayList<Document> documents = null;

}

class Document{
    String title;
    String contents;
    String url;
    String isbn;
    String datetime;
    ArrayList<String> authors = null;
    String publisher;
    ArrayList<String> translators =null;
    int price;
    int sale_price;
    String thumbnail;
    String status;

}

class Meta{
    int total_count;
    int pageable_count;
    boolean is_end;
}

//        {"meta":{"total_count":  "pageable_count"  "is_end"} "documents": [
//                {"title" "contents" "url" "isbn" "datetime" "authors":[0:string"기시미 이치로" 1:string"고가 후미타케"] "publisher": "translators":[0:string"전경아"] "price" "sale_price" "thumbnail"  "status"}
//        ]
//        }