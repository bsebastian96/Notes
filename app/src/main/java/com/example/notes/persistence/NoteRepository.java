package com.example.notes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notes.async.DeleteAsyncTask;
import com.example.notes.async.InsertAsyncTask;
import com.example.notes.async.UpdateAsyncTask;
import com.example.notes.model.Note;

import java.util.List;

/*
    Usually, a repository is best when you are retrieving data from multiple data sources,
    we are only using one data source -> SQLite; however, it's good to learn how to use one.

 */
public class NoteRepository {

    private NoteDatabase gNoteDatabase;

    public NoteRepository(Context context) {
        gNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note) {
        new InsertAsyncTask(gNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note) {
        new UpdateAsyncTask(gNoteDatabase.getNoteDao()).execute(note);
    }

    /*
        LiveData class provides a way to observe changes to data in real time
     */
    public LiveData<List<Note>> retrieveNotesTask() {

        return gNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note) {
        new DeleteAsyncTask(gNoteDatabase.getNoteDao()).execute(note);
    }
}
