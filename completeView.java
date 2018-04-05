package com.example.julianramirez.taskby;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Julian Ramirez on 3/29/2018.
 */

public class completeView extends ListActivity {

    public static final String ROW_ID = "row_id";
    private ListView taskListView;
    private CursorAdapter taskAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_list);

        taskListView = (ListView) findViewById(android.R.id.list);

        String[] from = new String[] { "fullTask" };
        int[] to = new int[] { R.id.trytextView};
        taskAdapter = new SimpleCursorAdapter(
                completeView.this, R.layout.backup, null, from, to);

        setListAdapter(taskAdapter);
        taskListView.setOnItemClickListener(viewTaskListener);
    }
    @Override
    protected void onResume() {
        super.onResume(); // call super's onResume method

        // create new GetContactsTask and execute it
        new completeView.GetTask().execute((Object[]) null);
    } // end onResume

    @Override
    protected void onStop (){
        Cursor cursor = taskAdapter.getCursor();

        if (cursor != null)
            cursor.deactivate();

        taskAdapter.changeCursor(null);
        super.onStop();
    }

    private class GetTask extends AsyncTask<Object, Object, Cursor> {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(completeView.this);

        @Override
        protected Cursor doInBackground(Object... params){
            databaseConnector.open();

            return databaseConnector.getAllCompletedTasks();
        }

        @Override
        protected void onPostExecute(Cursor result){
            taskAdapter.changeCursor(result); // set the adapter's Cursor
            databaseConnector.close();
        }
    }
    AdapterView.OnItemClickListener viewTaskListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg2, int position,
                                long arg3){

            Intent viewTask =
                    new Intent(completeView.this, completeViewTask.class);
            viewTask.putExtra(ROW_ID, arg3);
            startActivity(viewTask);

        }
    };
}
