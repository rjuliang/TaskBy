package com.example.julianramirez.taskby;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by Julian Ramirez on 3/28/2018.
 */

public class popUpWindow extends AppCompatActivity {
    private long rowID;
    public static final String ROW_ID = "row_id";

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText taskNameEditText;
    private EditText taskDetailsEditText;
    private EditText taskAddressEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        Button complete = (Button) findViewById(R.id.taskComplete);
        Button editView = (Button) findViewById(R.id.editViewTask);
        /*taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        taskDetailsEditText = (EditText) findViewById(R.id.taskDetailsEditText);
        taskAddressEditText = (EditText) findViewById(R.id.taskAddressEditText);*/
        editView.setOnClickListener(viewTaskClickListener);

        Bundle extras = getIntent().getExtras();
        rowID = extras.getLong(MainActivity.ROW_ID);
    }
    View.OnClickListener viewTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //((ListView) taskListView).performItemClick(null, 0, getListAdapter().getItemId(0));
            startActivity(new Intent(popUpWindow.this, viewTask.class));
        }
    };
    @Override
    protected void onResume()
    {
        super.onResume();
        new popUpWindow.LoadTask().execute(rowID);
    }
    private class LoadTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(popUpWindow.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params)
        {
            databaseConnector.open();


            return databaseConnector.getOneTask(params[0]);
        }


        @Override
        protected void onPostExecute(Cursor result)
        {
            super.onPostExecute(result);

            result.moveToFirst();

            // get the column index for each data item
            /*int nameIndex = result.getColumnIndex("name");
            int detailsIndex = result.getColumnIndex("details");
            int addressIndex = result.getColumnIndex("address");
            int fulldateIndex = result.getColumnIndex("fulldate");
            int monthIndex = result.getColumnIndex("month");
            int dayIndex = result.getColumnIndex("day");
            //details TEXT, address TEXT, fulldate CHAR, month CHAR, day CHAR

            taskNameEditText.setText(result.getString(nameIndex));
            taskDetailsEditText.setText(result.getString(detailsIndex));
            taskAddressEditText.setText(result.getString(addressIndex));*/

            result.close();
            databaseConnector.close();
        } // end method onPostExecute
    }
}
