package com.nirwal.epoint.models;

import java.util.Locale;

/**
 * Created by nirwal on 12/31/2017.
 */

public class ParentChildListItem {

    public String Id;
    public String ParentId;
    public String ChildId;

    public String ChildType;
    public String Title;
    public String Description;
    public String ImageUrl ;
    public byte[] Image;

    public int Favourites = 0;


    public boolean isVisible = false;
    public String Language;





    public ParentChildListItem(){
        // default constructor.

    }

    public ParentChildListItem(String id, String parentId, String childId,String childType,
                               String title, String description, String imageUrl,
                               boolean isVisible, String  language)
    {
        this.Id = id;
        this.ParentId = parentId;
        this.ChildId = childId;

        this.ChildType= childType;
        this.Title = title;
        this.Description = description;
        this.ImageUrl = imageUrl;

        this.isVisible = isVisible;
        this.Language =language;

    }

    public ParentChildListItem(String id, String parentId, String childId,
                               String title, String description, byte[] image,
                               boolean isVisible, String language)
    {
        this.Id = id;
        this.ParentId = parentId;
        this.ChildId = childId;
        this.Title = title;
        this.Description = description;
        this.Image = image;

        this.isVisible = isVisible;
        this.Language =language;

    }



}


