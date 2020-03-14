package com.example.notes.async;

import android.os.AsyncTask;

import com.example.notes.model.Note;
import com.example.notes.persistence.NoteDao;

/*
    Async task is for executing a single task in a background thread and then being destroyed.
 */
public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.deleteNotes(notes);
        return null;
    }
}