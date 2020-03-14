package com.example.notes.async;

import android.os.AsyncTask;

import com.example.notes.model.Note;
import com.example.notes.persistence.NoteDao;

/*
    Async task is for executing a single task in a background thread and then being destroyed.
 */
public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao gNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        gNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        gNoteDao.deleteNotes(notes);
        return null;
    }
}
