package com.nirwal.epoint.models;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Question implements Serializable{
    public String Id;
    public String ParentId;
    public String Question;
    public String Answer;
    public List<String> Options;
    public List<String> Images;

    //-- for exam purpose-------------

    public int questionNo;
    public boolean isCorrect=false;
    public boolean isAnswered =false;
    public String chosenOption="-1";



    public Question(){
        //default contructor
    }


    public Question(String id, String parentId, String question, String answer,
                    String[] options, String[] images)
    {
        this.Id = id;
        this.ParentId = parentId;
        this.Question = question;
        this.Answer = answer;
//        this.Options = options;
//        this.Images = images;

        this.Options = Arrays.asList(options);
        this.Images = Arrays.asList(images);
    }



    public String getOptionText(String a) {

        int pos = Integer.valueOf(a)-1;
        if(pos>=0 && pos<=this.Options.size()){
            return this.Options.get(pos);
        }
        return "";
    }

    public String getAnswerText() {
        return getOptionText(this.Answer);
    }


    public String getChosenOption(){
        return getOptionText(chosenOption);
    }

    public boolean isImageavailable()
    {
        return true ;//Images[0]!="/";
    }
}
