package com.example.notes.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.model.Note;

import java.util.List;

/*
    Contains all the methods responsible for accessing the database
    DATA ACCESS OBJECTS
 */
@Dao
public interface NoteDao {

    /*
        long[] is the return type.  If insertion succeeds, returns long array of inserted rows.
        If failed, {-1} is returned.  You can usually use void as return type
        ... is another way to denote [].  It is an optional list, so it could also be just 1 note
     */
    @Insert
    long[] insertNotes(Note... notes);

    /*
        Takes custom SQL query as argument.
        LiveData object holds a List of notes.  LiveData is a data observation class, part of the
        lifecycle library in Android.
     */
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    List<Note> getNoteWithID(int id);

    // LIKE allows user to search for a title w/o knowing the correct spelling (eli* for Elizabeth)
    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Note> getNoteWithTitle(String title);

    /*
        int return type will return a number that represents how many rows were deleted
     */
    @Delete
    int deleteNotes(Note... notes);

    @Update
    int updateNotes(Note...notes);
}
