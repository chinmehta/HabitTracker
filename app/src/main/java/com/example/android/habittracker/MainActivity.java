package com.example.android.habittracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittracker.data.ContractClass.HabitEntry;
import com.example.android.habittracker.data.DBHelper;

import static com.example.android.habittracker.data.ContractClass.HabitEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TextView displayInfo;
    SQLiteDatabase db;
    Cursor cursor;
    int currentID;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
        mDbHelper = new DBHelper(this);
    }


    // Helper method to display information in the TextView about the state of the database
    private void displayDatabaseInfo() {

        mDbHelper = new DBHelper(this);

        // Create and/or open a database to read from it
        db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table
        String[] projection = {HabitEntry._ID, HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_STATUS, HabitEntry.COLUMN_HABIT_DURATION};

        cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        displayInfo = (TextView) findViewById(R.id.habit_text);

        try {
            displayInfo.setText("There are " + cursor.getCount() + " in the table\n\n");
            displayInfo.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_STATUS + " - " +
                    HabitEntry.COLUMN_HABIT_DURATION + "\n");

            // Index of each column
            int idIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int levelIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_STATUS);
            int durationIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DURATION);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                currentID = cursor.getInt(idIndex);
                String currentName = cursor.getString(nameIndex);
                int currentLevel = cursor.getInt(levelIndex);
                int currentDuration = cursor.getInt(durationIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayInfo.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentLevel + " - " +
                        currentDuration));
            }

        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    public boolean deleteData() {
        db = mDbHelper.getWritableDatabase();
        db.delete(HabitEntry.TABLE_NAME, null, null);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // click on the "Insert Habit Data" menu option
            case R.id.insert_habit_data:
                displayDatabaseInfo();
                return true;
            // click on the "Delete all data" menu option
            case R.id.delete_all_data:
                deleteData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
