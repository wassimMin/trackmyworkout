package com.example.trackwokrout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DayActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    Button btnAddExercise = findViewById(R.id.btn_add_exercise);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
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
        EditText set_sets = exerciseSection.findViewById(R.id.set_sets);
        LinearLayout setContainer = exerciseSection.findViewById(R.id.set_container);
        set_sets.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                }
            }
        });
    }
    private void addSetInput(){

    }
}