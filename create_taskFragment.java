package com.example.julianramirez.taskby;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {//@link create_taskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {//@link create_taskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_taskFragment extends AppCompatActivity {

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_main);

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

        if (extras != null){
            rowID = extras.getLong("row_id");
            taskNameEditText.setText(extras.getString("name"));
            taskDetailsEditText.setText(extras.getString("details"));
            taskAddressEditText.setText(extras.getString("address"));
            //String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String day = " " + datePicker.getDayOfMonth();
            String month = " " + (datePicker.getMonth() + 1);
            String year = " " + datePicker.getYear();
            extras.getString("day");
            //String time = " " + timePicker.getHour();
            extras.getString("year");
            //day = extras.getString("day");
            extras.getString("month");

            hourEditText.setText(extras.getString("hour"));
            minutesEditText.setText(extras.getString("minutes"));
            String fullDate = month +" "+ day + " "+year;
        }
        Button saveButton =
                 (Button) findViewById(R.id.saveButton);
         saveButton.setOnClickListener(saveButtonClicked);
    }
    OnClickListener saveButtonClicked = new OnClickListener(){
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
                        new AlertDialog.Builder(create_taskFragment.this);

                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        }
    };

    private void saveTask(){
        String timeOfDay = "";
        boolean checked = AM.isChecked();
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
        fullTask = taskNameEditText.getText().toString() + "      " + fullDate + "       at "
                + hourEditText.getText().toString()+ ":" + minutesEditText.getText().toString() + " " + timeOfDay;
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
}
