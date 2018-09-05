package com.nirwal.epoint.models;


public class QuizLists {
    public String Id;
    public String ParentId;
    public String QuestionListId;
    public String Title;
    public String Imagepath;
    public String Description;
    public boolean isVisible = false;


    public QuizLists(){
        //default cunstructor
    }

    public QuizLists(String id, String parentId,  String questionListId,
                     String title, String imagePath, String description /* ,
                     boolean isVisible */) {

        this.Id = id;
        this.ParentId = parentId;
        this.Title = title;
        this.Description = description;
        this.Imagepath = imagePath;
        this.QuestionListId = questionListId;
        //this.isVisible = isVisible;
    }


}