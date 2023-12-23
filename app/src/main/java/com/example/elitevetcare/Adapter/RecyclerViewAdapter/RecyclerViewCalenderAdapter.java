package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.R;

import java.util.ArrayList;

public class RecyclerViewCalenderAdapter extends RecyclerView.Adapter<RecyclerViewCalenderAdapter.CalenderViewHolder> {

    private ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RecyclerViewCalenderAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_calender_cell, parent, false);
        return new CalenderViewHolder(view, onItemListener);
    }
    public void setDataList(ArrayList<String> daysOfMonth)
    {
        this.daysOfMonth = daysOfMonth;
    }
    @Override
    public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.setSelected(selectedPosition == position);
    }
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView khi trạng thái thay đổi
    }
    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, TextView dayText, View parent);
    }
    public class CalenderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView dayOfMonth;
        ConstraintLayout parent;
        private final RecyclerViewCalenderAdapter.OnItemListener onItemListener;
        private boolean isSelected = false;
        public void setSelected(boolean selected) {
            isSelected = selected;
            updateUI();
        }

        private void updateUI() {
            if(isSelected == false)
                return;
            dayOfMonth.setTextColor(parent.getResources().getColor(R.color.white));
            parent.setBackgroundResource(R.drawable.custom_selected_bar);
        }

        public CalenderViewHolder(@NonNull View itemView, RecyclerViewCalenderAdapter.OnItemListener onItemListener)
        {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            parent = ((ConstraintLayout) dayOfMonth.getParent());
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onItemListener.onItemClick(getAdapterPosition(), dayOfMonth,parent);
        }

    }
}
