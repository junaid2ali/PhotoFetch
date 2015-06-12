package com.cestar.actor;

/**
 * Created by josephmoscatiello1 on 15-06-12.
 */
public class PhotoImage {

    String name, category, comments, path;

    public PhotoImage(String name, String category, String comments, String path)
    {
        this.name = name;
        this.category = category;
        this.comments = comments;
        this.path = path;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



}
