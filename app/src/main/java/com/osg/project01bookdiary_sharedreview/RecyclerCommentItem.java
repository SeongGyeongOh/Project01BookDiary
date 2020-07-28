package com.osg.project01bookdiary_sharedreview;

public class RecyclerCommentItem {

    String nickName, comment;

    public RecyclerCommentItem(String nickName, String comment) {
        this.nickName = nickName;
        this.comment = comment;
    }

    public RecyclerCommentItem(String nickName){
        this.nickName=nickName;
    }

    public RecyclerCommentItem() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
