package com.nirwal.epoint.models;

/**
 * Created by nirwal on 12/31/2017.
 */

public class MainCard {


    public String Title;
    public String Description;
    public String Imagepath ;
    public String QuizListId;
    public String Id;
    //public boolean isVisible = false;

    public MainCard(){
        // default constructor.
    }

    public MainCard(String id, String title, String description, String imagePath, String quizListId /* , boolean isVisible*/)
    {
        this.Id=id;
        this.Title = title;
        this.Description = description;
        this.Imagepath = imagePath;
        this.QuizListId = quizListId;
       // this.isVisible = isVisible;

    }


}
