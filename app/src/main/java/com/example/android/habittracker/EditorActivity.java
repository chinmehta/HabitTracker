package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittracker.data.ContractClass.HabitEntry;
import com.example.android.habittracker.data.DBHelper;

public class EditorActivity extends AppCompatActivity {


    private EditText mHabitName;
    private Spinner mHabitSpinner;
    private EditText mHabitDuration;
    private int mHabitLevel = HabitEntry.STATUS_UNKNOWN;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mHabitName = (EditText) findViewById(R.id.put_habit_name);
        mHabitDuration = (EditText) findViewById(R.id.put_habit_duration);
        mHabitSpinner = (Spinner) findViewById(R.id.spinner);
        setupSpinner();
    }

    // Set the
    // dropdown spinner
    private void setupSpinner() {
        // Create an adapter
        // for spinner
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_scale, android.R.layout.simple_spinner_item);

        // Specify the dropdown
        // layout style
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Set adapter to the spinner
        mHabitSpinner.setAdapter(genderSpinnerAdapter);

        mHabitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.habit_scale_good))) {
                        mHabitLevel = HabitEntry.STATUS_GOOD;
                    } else if (selection.equals(getString(R.string.habit_scale_very_good))) {
                        mHabitLevel = HabitEntry.STATUS_VERY_GOOD;
                    } else if (selection.equals(getString(R.string.habit_scale_bad))) {
                        mHabitLevel = HabitEntry.STATUS_BAD;
                    } else {
                        mHabitLevel = HabitEntry.STATUS_VERY_BAD;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mHabitLevel = HabitEntry.STATUS_UNKNOWN;
            }
        });
    }

    private void insertPet() {
        String nameString = mHabitName.getText().toString().trim();
        String durationString = mHabitDuration.getText().toString().trim();
        int duration = Integer.parseInt(durationString);

        DBHelper mDbHelper = new DBHelper(this);
        db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_STATUS, mHabitLevel);
        values.put(HabitEntry.COLUMN_HABIT_DURATION, duration);

        long newRowID = db.insert(HabitEntry.TABLE_NAME, null, values);
        if (newRowID == -1) {
            Toast.makeText(this, "Error with creating database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You have " + newRowID + " habits", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // click on the "Save" menu option
            case R.id.action_save:
                insertPet();
                finish();
                return true;
            // click on the "Delete" menu option
            case R.id.action_delete:
                return true;
            // click on the "Up" arrow button
            case android.R.id.home:
                // Navigate back
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}