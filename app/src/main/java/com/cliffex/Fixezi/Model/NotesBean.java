package com.cliffex.Fixezi.Model;

/**
 * Created by technorizen8 on 5/4/16.
 */
public class NotesBean {

    private String id;
    private String notes_description;
    private String notes_type;
    private String notes_image_ratio;
    private String notes_image;
    private String timeonjob;

    public String getTimeonjob() {
        return timeonjob;
    }

    public void setTimeonjob(String timeonjob) {
        this.timeonjob = timeonjob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes_description() {
        return notes_description;
    }

    public void setNotes_description(String notes_description) {
        this.notes_description = notes_description;
    }

    public String getNotes_image() {
        return notes_image;
    }

    public void setNotes_image(String notes_image) {
        this.notes_image = notes_image;
    }

    public String getNotes_type() {
        return notes_type;
    }

    public void setNotes_type(String notes_type) {
        this.notes_type = notes_type;
    }

    public String getNotes_image_ratio() {
        return notes_image_ratio;
    }

    public void setNotes_image_ratio(String notes_image_ratio) {
        this.notes_image_ratio = notes_image_ratio;
    }
}
