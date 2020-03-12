package com.example.notes.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.model.Note;

/*
    The Note class defines the entity (set of fields for DB) we are using.  This class creates
    a schema folder that outputs the scheme of every version we had.  Change the version number
    if you are updating the entities and all the schemas will be tracked.
 */
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    private static NoteDatabase instance;
    /*
        Using a singleton pattern which creates an instance of NoteDatabase and checks whether
        it is null.  This prevents multiple instances of the same object being present in memory.
        Good way to optimize memory usage.
     */
    static NoteDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }

        return instance;
    }

    // Returns a reference to the Dao
    public abstract NoteDao getNoteDao();
}
