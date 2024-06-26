package com.example.trackwokrout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<Day> dayList;
    private Context context;

    public DayAdapter(List<Day> dayList, Context context) {
        this.dayList = dayList;
        this.context = context;
    }
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = dayList.get(position);
        holder.buttonDay.setText(day.getDayName());
        holder.buttonDay.setOnClickListener(v -> {
            Intent intent = new Intent(context, DayActivity.class);
            intent.putExtra("dayName", day.getDayName());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        Button buttonDay;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonDay = itemView.findViewById(R.id.buttonDay);
        }
    }
}
