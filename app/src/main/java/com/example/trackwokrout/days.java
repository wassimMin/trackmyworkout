package com.example.trackwokrout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class days extends AppCompatActivity {
    private RecyclerView recyclerViewDays;
    private DayAdapter dayAdapter;
    private List<Day> dayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);
        recyclerViewDays = findViewById(R.id.recyclerViewDays);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this));
        dayList = new ArrayList<>();
        for(int i=1;i<=7;i++){
            dayList.add(new Day("Day "+ i));
        }
        dayAdapter = new DayAdapter(dayList,this);
        recyclerViewDays.setAdapter(dayAdapter);
    }
}