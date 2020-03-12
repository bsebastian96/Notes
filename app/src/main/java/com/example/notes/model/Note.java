package com.example.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/*
    Note class will be a data model for the notes.
    In order to pass a Note object from one activity to another, you need to implement
    parcelable.  Otherwise, the new class object will not be able to be attached to a bundle to
    send through an intent
 */
@Entity(tableName = "notes")
public class Note  implements Parcelable {

    // id is the primary key for notes table, id will be auto generated if not specified
    @PrimaryKey(autoGenerate = true)
    private int id;

    /*
        You can also add @Nonnull above each of the variables below to tell our DB to not have any
        null values in the DB.  PrimaryKey cannot be null so no need to attach it there.
     */
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "timestamp")
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
        Empty Constructor.  We need to tell the Room persistence library which constructor to use so
        we add @Ignore to this constructor because we want our DB to use the parameterized one
     */
    @Ignore
    public Note() {

    }

    protected Note(Parcel in) {
        id = in.readInt();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(timestamp);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
