package com.example.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Note class will be a data model for the notes.
    In order to pass a Note object from one activity to another, you need to implement
    parcelable.  Otherwise, the new class object will not be able to be attached to a bundle to
    send through an intent
 */
public class Note  implements Parcelable {

    private String title;
    private String content;
    private String timestamp;

    /*
    Default constructor
    Alt+Ins: shortcut for creation
     */
    public Note(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    /*
    Empty Constructor
     */
    public Note() {

    }

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(timestamp);
    }
}
