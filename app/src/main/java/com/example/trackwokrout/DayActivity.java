package com.example.trackwokrout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    Intent intent;
    String dayName;
    Button btnAddExercise = findViewById(R.id.btn_add_exercise);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        intent = getIntent();
        dayName = intent.getStringExtra("dayName");
        exerciseContainer = findViewById(R.id.exercise_container);
        btnAddExercise = findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseSection();
            }
        });
    }
    private void addExerciseSection(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View exerciseSection = inflater.inflate(R.layout.exercise_input_section, exerciseContainer,false);

        Spinner spinnerSets = exerciseSection.findViewById(R.id.spinner_sets);
        LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);

        int numSets = Integer.parseInt(spinnerSets.getSelectedItem().toString());
        for (int i = 0; i < numSets; i++) {
            View setView = inflater.inflate(R.layout.set_input, setContainer, false);
            setContainer.addView(setView);
        }

        exerciseContainer.addView(exerciseSection);
        saveExercises();
    }
    private void saveExercises(){
        SharedPreferences sharedPreferences = getSharedPreferences("workout", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray exercisesArray = new JSONArray();
        for(int i=0;i<exerciseContainer.getChildCount();i++){
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
                throw new RuntimeException(e);
            }
            exercisesArray.put(exerciseObject);
        }
        editor.putString(dayName, exercisesArray.toString());
        editor.apply();
    }
    private void loadExercises(){
        SharedPreferences sharedPreferences = getSharedPreferences("workout", Context.MODE_PRIVATE);
        String exercisesString = sharedPreferences.getString(dayName, "[]");
        try {
            JSONArray exercisesArray = new JSONArray(exercisesString);
            for(int i=0;i<exercisesArray.length();i++){
                JSONObject exercsiseObject = exercisesArray.getJSONObject(i);
                LayoutInflater inflater = LayoutInflater.from(this);
                View exerciseSection = inflater.inflate(R.layout.exercise_input_section, exerciseContainer, false);


                EditText etExerciseName = exerciseSection.findViewById(R.id.et_exercise_name);
                Spinner spinnerSets = exerciseSection.findViewById(R.id.spinner_sets);
                LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);

                etExerciseName.setText(exercsiseObject.getString("exerciseName"));
                spinnerSets.setSelection(getSpinnerIndex(spinnerSets, exercsiseObject.getString("numSets")));
                JSONArray setsArray = exercsiseObject.getJSONArray("sets");
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}