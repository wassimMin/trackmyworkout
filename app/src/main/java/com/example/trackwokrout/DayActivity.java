package com.example.trackwokrout;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    Intent intent;
    String dayName;
    Button btnAddExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        intent = getIntent();
//         clearSharedPreferences();
        dayName = intent.getStringExtra("dayName");
        exerciseContainer = findViewById(R.id.exercise_container);
        btnAddExercise = findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseSection();
            }
        });
        loadExercises();
    }

    private void addExerciseSection() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View exerciseSection = inflater.inflate(R.layout.exercise_input_section, exerciseContainer, false);
        Log.d(TAG, "Exercise section inflated.");

        Spinner spinnerSets = exerciseSection.findViewById(R.id.spinner_sets);
        LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);

        spinnerSets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numSets = Integer.parseInt(parent.getItemAtPosition(position).toString());
                setContainer.removeAllViews();
                for (int i = 0; i < numSets; i++) {
                    View setView = inflater.inflate(R.layout.set_input, setContainer, false);
                    setContainer.addView(setView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageButton btnRemoveExercise = exerciseSection.findViewById(R.id.btn_remove_exercise);
        btnRemoveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Remove button clicked.");
                exerciseContainer.removeView(exerciseSection);
                saveExercises();
            }
        });

        exerciseContainer.addView(exerciseSection);
        saveExercises();
    }

    private void saveExercises() {
        SharedPreferences sharedPreferences = getSharedPreferences("workout", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray exercisesArray = new JSONArray();

        for (int i = 0; i < exerciseContainer.getChildCount(); i++) {
            View exerciseSection = exerciseContainer.getChildAt(i);
            EditText etExerciseName = exerciseSection.findViewById(R.id.et_exercise_name);
            Spinner spinnerSets = exerciseSection.findViewById(R.id.spinner_sets);
            LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);

            JSONObject exerciseObject = new JSONObject();
            try {
                exerciseObject.put("exerciseName", etExerciseName.getText().toString());
                exerciseObject.put("numSets", spinnerSets.getSelectedItem().toString());
                JSONArray setsArray = new JSONArray();
                for (int j = 0; j < setContainer.getChildCount(); j++) {
                    View setView = setContainer.getChildAt(j);
                    EditText etReps = setView.findViewById(R.id.set_reps);
                    EditText etWeight = setView.findViewById(R.id.set_weight);
                    Spinner spinnerWeightUnit = setView.findViewById(R.id.spinner_weight_unit);

                    JSONObject setObject = new JSONObject();
                    setObject.put("reps", etReps.getText().toString());
                    setObject.put("weight", etWeight.getText().toString());
                    setObject.put("weightUnit", spinnerWeightUnit.getSelectedItem().toString());

                    setsArray.put(setObject);
                }
                exerciseObject.put("sets", setsArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            exercisesArray.put(exerciseObject);
        }

        editor.putString(dayName, exercisesArray.toString());
        editor.apply();
    }

    private void loadExercises() {
        SharedPreferences sharedPreferences = getSharedPreferences("workout", Context.MODE_PRIVATE);
        String exercisesString = sharedPreferences.getString(dayName, "[]");
        try {
            JSONArray exercisesArray = new JSONArray(exercisesString);
            for (int i = 0; i < exercisesArray.length(); i++) {
                JSONObject exerciseObject = exercisesArray.getJSONObject(i);
                LayoutInflater inflater = LayoutInflater.from(this);
                View exerciseSection = inflater.inflate(R.layout.exercise_input_section, exerciseContainer, false);

                EditText etExerciseName = exerciseSection.findViewById(R.id.et_exercise_name);
                Spinner spinnerSets = exerciseSection.findViewById(R.id.spinner_sets);
                LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);

                etExerciseName.setText(exerciseObject.getString("exerciseName"));
                spinnerSets.setSelection(getSpinnerIndex(spinnerSets, exerciseObject.getString("numSets")));
                JSONArray setsArray = exerciseObject.getJSONArray("sets");
                for (int j = 0; j < setsArray.length(); j++) {
                    JSONObject setObject = setsArray.getJSONObject(j);

                    View setView = inflater.inflate(R.layout.set_input, setContainer, false);

                    EditText etReps = setView.findViewById(R.id.set_reps);
                    EditText etWeight = setView.findViewById(R.id.set_weight);
                    Spinner spinnerWeightUnit = setView.findViewById(R.id.spinner_weight_unit);

                    etReps.setText(setObject.getString("reps"));
                    etWeight.setText(setObject.getString("weight"));
                    spinnerWeightUnit.setSelection(getSpinnerIndex(spinnerWeightUnit, setObject.getString("weightUnit")));

                    setContainer.addView(setView);
                }

                // Set the remove button listener again
                ImageButton btnRemoveExercise = exerciseSection.findViewById(R.id.btn_remove_exercise);
                btnRemoveExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Remove button clicked.");
                        exerciseContainer.removeView(exerciseSection);
                        saveExercises();
                    }
                });

                exerciseContainer.addView(exerciseSection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("workout", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
