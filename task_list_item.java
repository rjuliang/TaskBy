package com.example.julianramirez.taskby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Julian Ramirez on 3/24/2018.
 */

public class task_list_item extends Activity {

    private ImageButton editTaskButton;
    private AppCompatImageView sharetask;
    private AppCompatImageView deleteTask;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_item);

        editTaskButton = (ImageButton) findViewById(R.id.editTaskButton);
        sharetask = (AppCompatImageView) findViewById(R.id.sharetask);
        deleteTask = (AppCompatImageView) findViewById(R.id.deleteTask);

        editTaskButton.setOnClickListener(viewTaskClickListener);

    }
    View.OnClickListener viewTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //((ListView) taskListView).performItemClick(null, 0, getListAdapter().getItemId(0));
            startActivity(new Intent(task_list_item.this, viewTask.class));
        }
    };
}
