package com.example.julianramirez.taskby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.Toolbar;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    public static final String ROW_ID = "row_id";
    private ListView taskListView;
    private CursorAdapter taskAdapter;
    private ArrayList<String> data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        taskListView = (ListView) findViewById(android.R.id.list);
        //taskListView = getListView();


        //String[] from = new String[] { "name" , "fulldate" };
        //int[] to = new int[] { R.id.checkBoxTaskName, R.id.dateTextView};
        String[] from = new String[] { "fullTask" };
        int[] to = new int[] { R.id.trytextView};
                taskAdapter = new SimpleCursorAdapter(
                MainActivity.this, R.layout.backup, null, from, to);

        setListAdapter(taskAdapter);
        taskListView.setOnItemClickListener(viewTaskListener);


        FloatingActionButton plusButton =
                (FloatingActionButton) findViewById(R.id.plusButton);
        Button completeButton = (Button) findViewById(R.id.completeButton);

        completeButton.setOnClickListener(completeOnClickListener);

        plusButton.setOnClickListener(onClickListener);
        /*taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                //Log.v("long clicked","pos: " + pos);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.confirmTitle);
                builder.setMessage(R.string.confirmMessage);

                builder.setPositiveButton("Edit",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int button)
                            {

                            }
                        }

                );
                builder.setNegativeButton(R.string.button_cancel, null);
                builder.show();


                String task = "Hello!";
                Toast toast = Toast.makeText(MainActivity.this, task, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            }
        });*/


    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, create_taskFragment.class));
        }
    };
    View.OnClickListener completeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, completeView.class));
        }
    };


    @Override
      protected void onResume() {
         super.onResume(); // call super's onResume method

         // create new GetContactsTask and execute it
         new GetTask().execute((Object[]) null);
    } // end onResume

    @Override
      protected void onStop (){
        Cursor cursor = taskAdapter.getCursor();

        if (cursor != null)
            cursor.deactivate();

        taskAdapter.changeCursor(null);
        super.onStop();
    }

    private class GetTask extends AsyncTask<Object, Object, Cursor>{
        DatabaseConnector databaseConnector =
                new DatabaseConnector(MainActivity.this);

        @Override
        protected Cursor doInBackground(Object... params){
            databaseConnector.open();

            return databaseConnector.getAllTasks();
        }

        @Override
        protected void onPostExecute(Cursor result){
            taskAdapter.changeCursor(result); // set the adapter's Cursor
            databaseConnector.close();
        }
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent addNewTask =
                new Intent(MainActivity.this, create_taskFragment.class);
        startActivity(addNewTask);
        return super.onOptionsItemSelected(item);

    }

    OnItemClickListener viewTaskListener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg2, int position,
                                long arg3){

                Intent viewTask =
                        new Intent(MainActivity.this, viewTask.class);
                viewTask.putExtra(ROW_ID, arg3);
                startActivity(viewTask);

        }
    };


}
