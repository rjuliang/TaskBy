package com.example.julianramirez.taskby;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TimePicker;

public class completeViewTask extends AppCompatActivity {
    private long rowID;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText taskNameEditText;
    private EditText taskDetailsEditText;
    private EditText taskAddressEditText;
    private EditText hourEditText;
    private EditText minutesEditText;
    private RadioButton AM;
    private RadioButton PM;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        //timePicker = (TimePicker) findViewById(R.id.timePicker);
        taskDetailsEditText = (EditText) findViewById(R.id.taskDetailsEditText);
        taskAddressEditText = (EditText) findViewById(R.id.taskAddressEditText);
        hourEditText = (EditText) findViewById(R.id.hourEditText);
        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        AM = (RadioButton) findViewById(R.id.AMradioButton);
        PM = (RadioButton) findViewById(R.id.PMradioButton);

        Bundle extras = getIntent().getExtras();
        rowID = extras.getLong(MainActivity.ROW_ID);

        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteButtonClicked);

        Button completeButton = (Button) findViewById(R.id.completeButton);
        completeButton.setText("MARK TASK AS INCOMPLETE");
        completeButton.setOnClickListener(completeButtonClicked);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonClicked);
    }
    View.OnClickListener deleteButtonClicked = new View.OnClickListener(){
        public void onClick(View v){
            deleteTask();
        }
    };
    View.OnClickListener completeButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if (taskNameEditText.getText().length() != 0){
                AsyncTask<Object, Object, Object> saveTask =
                        new AsyncTask<Object, Object, Object>(){
                            @Override
                            protected Object doInBackground(Object... params){
                                markinCompleted();
                                deleteTaskComplete();
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Object result){
                                finish();
                            }
                        };
                saveTask.execute((Object[]) null);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(completeViewTask.this);

                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }


        }
    };
    private void deleteTaskComplete()
    {

        final DatabaseConnector databaseConnector =
                new DatabaseConnector(completeViewTask.this);

        // create an AsyncTask that deletes the contact in another
        // thread, then calls finish after the deletion
        AsyncTask<Long, Object, Object> deleteTask =
                new AsyncTask<Long, Object, Object>()
                {
                    @Override
                    protected Object doInBackground(Long... params)
                    {
                        databaseConnector.deleteCompletedTask(params[0]);
                        return null;
                    } // end method doInBackground

                    @Override
                    protected void onPostExecute(Object result)
                    {
                        finish(); // return to the AddressBook Activity
                    } // end method onPostExecute
                }; // end new AsyncTask

        // execute the AsyncTask to delete contact at rowID
        deleteTask.execute(new Long[] { rowID });

        //builder.setNegativeButton(R.string.button_cancel, null);
    } // end method deleteContact
    private void markinCompleted() {
        String timeOfDay = "";
        if(AM.isChecked()){
            timeOfDay = "AM";
        } else {
            timeOfDay = "PM";
        }
        DatabaseConnector databaseConnector = new DatabaseConnector(this);
        String day = "" + datePicker.getDayOfMonth();
        String monthNumber = "" + (datePicker.getMonth() + 1);
        String year = "" + datePicker.getYear();
        String monthLetter =" ";
        String fullTask = "";
        //String time = " " + timePicker.getHour();
        if(monthNumber.equals("1")){
            monthLetter = "Jan";
        } else if (monthNumber.equals("2")){
            monthLetter = "Feb";
        } else if (monthNumber.equals("3") ){
            monthLetter = "Mar";
        }else if (monthNumber.equals("4")){
            monthLetter = "Apr";
        } else if (monthNumber.equals("5")){
            monthLetter = "May";
        }else if (monthNumber.equals("6")){
            monthLetter = "Jun";
        }else if (monthNumber.equals("7")){
            monthLetter = "Jul";
        }else if (monthNumber.equals("8")){
            monthLetter = "Aug";
        }else if (monthNumber.equals("9")){
            monthLetter = "Sept";
        }else if (monthNumber.equals("10")){
            monthLetter = "Oct";
        }else if (monthNumber.equals("11")){
            monthLetter = "Nov";
        }else if (monthNumber.equals("12")){
            monthLetter = "Dic";
        }
        String fullDate = monthLetter+ " " + day;
        fullTask = taskNameEditText.getText().toString() + "      " + fullDate + "       at "
                + hourEditText.getText().toString()+ ":" + minutesEditText.getText().toString() + " " + timeOfDay ;

            databaseConnector.insertTask(
                    fullTask,
                    taskNameEditText.getText().toString(),
                    taskDetailsEditText.getText().toString(),
                    taskAddressEditText.getText().toString(),
                    fullDate,
                    monthNumber,
                    day,
                    year,
                    hourEditText.getText().toString(),
                    minutesEditText.getText().toString(),
                    timeOfDay);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        new completeViewTask.LoadTask().execute(rowID);
    }
    private class LoadTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(completeViewTask.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params)
        {
            databaseConnector.open();


            return databaseConnector.getOneCompletedTask(params[0]);
        }


        @Override
        protected void onPostExecute(Cursor result)
        {
            super.onPostExecute(result);

            result.moveToFirst();

            // get the column index for each data item
            int nameIndex = result.getColumnIndex("name");
            int detailsIndex = result.getColumnIndex("details");
            int addressIndex = result.getColumnIndex("address");
            int fulldateIndex = result.getColumnIndex("fulldate");
            int monthIndex = result.getColumnIndex("month");
            int dayIndex = result.getColumnIndex("day");
            int yearIndex = result.getColumnIndex("year");
            int hourIndex = result.getColumnIndex("hours");
            int minutesIndex = result.getColumnIndex("minutes");
            int timeOfDayIndex = result.getColumnIndex("timeOfDay");
            //details TEXT, address TEXT, fulldate CHAR, month CHAR, day CHAR


            taskNameEditText.setText(result.getString(nameIndex));
            taskDetailsEditText.setText(result.getString(detailsIndex));
            taskAddressEditText.setText(result.getString(addressIndex));
            hourEditText.setText(result.getString(hourIndex));
            minutesEditText.setText(result.getString(minutesIndex));
            String timeOfDay = result.getString(timeOfDayIndex);

            if(timeOfDay.equals("AM")){
                AM.setChecked(true);
            } else {
                PM.setChecked(true);
            }
            String month = result.getString(monthIndex);
            String day = result.getString(dayIndex);
            String year = result.getString(yearIndex);
            datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
            result.close();
            databaseConnector.close();
        } // end method onPostExecute
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    View.OnClickListener saveButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if (taskNameEditText.getText().length() != 0){
                AsyncTask<Object, Object, Object> saveTask =
                        new AsyncTask<Object, Object, Object>(){
                            @Override
                            protected Object doInBackground(Object... params){
                                saveTask();
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Object result){
                                finish();
                            }
                        };
                saveTask.execute((Object[]) null);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(completeViewTask.this);

                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        }
    };

    private void saveTask(){
        String timeOfDay = "";
        if(AM.isChecked()){
            timeOfDay = "AM";
        } else {
            timeOfDay = "PM";
        }
        DatabaseConnector databaseConnector = new DatabaseConnector(this);
        String day = "" + datePicker.getDayOfMonth();
        String monthNumber = "" + (datePicker.getMonth() + 1);
        String year = "" + datePicker.getYear();
        String monthLetter =" ";
        String fullTask = " ";
        //String time = " " + timePicker.getHour();
        if(monthNumber.equals("1")){
            monthLetter = "Jan";
        } else if (monthNumber.equals("2")){
            monthLetter = "Feb";
        } else if (monthNumber.equals("3") ){
            monthLetter = "Mar";
        }else if (monthNumber.equals("4")){
            monthLetter = "Apr";
        } else if (monthNumber.equals("5")){
            monthLetter = "May";
        }else if (monthNumber.equals("6")){
            monthLetter = "Jun";
        }else if (monthNumber.equals("7")){
            monthLetter = "Jul";
        }else if (monthNumber.equals("8")){
            monthLetter = "Aug";
        }else if (monthNumber.equals("9")){
            monthLetter = "Sept";
        }else if (monthNumber.equals("10")){
            monthLetter = "Oct";
        }else if (monthNumber.equals("11")){
            monthLetter = "Nov";
        }else if (monthNumber.equals("12")){
            monthLetter = "Dic";
        }
        String fullDate = monthLetter+ " " + day;
        fullTask = taskNameEditText.getText().toString() + "    " + fullDate + "    at   "
                + hourEditText.getText().toString()+ ":" + minutesEditText.getText().toString() + " " +timeOfDay;
        if (getIntent().getExtras() == null){
            databaseConnector.insertTask(
                    fullTask,
                    taskNameEditText.getText().toString(),
                    taskDetailsEditText.getText().toString(),
                    taskAddressEditText.getText().toString(),
                    fullDate,
                    monthNumber,
                    day,
                    year,
                    hourEditText.getText().toString(),
                    minutesEditText.getText().toString(),
                    timeOfDay);
        } else {
            databaseConnector.updateTask(rowID,
                    fullTask,
                    taskNameEditText.getText().toString(),
                    taskDetailsEditText.getText().toString(),
                    taskAddressEditText.getText().toString(),
                    fullDate,
                    monthNumber,
                    day,
                    year,
                    hourEditText.getText().toString(),
                    minutesEditText.getText().toString(),
                    timeOfDay);
        }
    }
    private void deleteTask()
    {
        // create a new AlertDialog Builder
        AlertDialog.Builder builder =
                new AlertDialog.Builder(completeViewTask.this);

        builder.setTitle(R.string.confirmTitle); // title bar string
        builder.setMessage(R.string.confirmMessage); // message to display

        // provide an OK button that simply dismisses the dialog
        builder.setPositiveButton(R.string.button_delete,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int button)
                    {
                        final DatabaseConnector databaseConnector =
                                new DatabaseConnector(completeViewTask.this);

                        // create an AsyncTask that deletes the contact in another
                        // thread, then calls finish after the deletion
                        AsyncTask<Long, Object, Object> deleteTask =
                                new AsyncTask<Long, Object, Object>()
                                {
                                    @Override
                                    protected Object doInBackground(Long... params)
                                    {
                                        databaseConnector.deleteTask(params[0]);
                                        return null;
                                    } // end method doInBackground

                                    @Override
                                    protected void onPostExecute(Object result)
                                    {
                                        finish(); // return to the AddressBook Activity
                                    } // end method onPostExecute
                                }; // end new AsyncTask

                        // execute the AsyncTask to delete contact at rowID
                        deleteTask.execute(new Long[] { rowID });
                    } // end method onClick
                } // end anonymous inner class
        ); // end call to method setPositiveButton

        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show(); // display the Dialog
    } // end method deleteContact
    public void Clicked(View view)
    {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,taskNameEditText.getText().toString());
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent,"Share via");
        startActivity(sendIntent);
    }
}
