package com.yujongu.socialserviceagent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter {

    private ArrayList<Day_Event> mCalList;

    private Context context;

    public int selectedDateIndex = -1;

    public CalendarAdapter(Context context, ArrayList<Day_Event> calendarList) {
        this.context = context;
        this.mCalList = calendarList;

        for (int i = 0; i < calendarList.size(); i++){
            if (calendarList.get(i).isClicked() == true){
                selectedDateIndex = i;
            }
        }

    }

    public void setCalendarList(ArrayList<Day_Event> calendarList) {
        this.mCalList = calendarList;
        notifyDataSetChanged();
    }



    @Override
    public int getItemViewType(int position) {
        Day_Event day = mCalList.get(position);
        return day.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == Day_Event.DAY){
            return new DayViewHolder(inflater.inflate(R.layout.calendar_item_day, parent, false));
        }
        return new EmptyViewHolder(inflater.inflate(R.layout.calendar_item_prev_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == Day_Event.DAY){
            DayViewHolder holder = (DayViewHolder) viewHolder;
            if (mCalList.get(position).isClicked()){
                holder.itemView.setBackgroundResource(R.drawable.cell_selected_background);

            } else {
                holder.itemView.setBackgroundResource(R.drawable.cell_border_background);
            }
            Day_Event item = mCalList.get(position);
            holder.itemDay.setText(String.valueOf(item.getDate()));

            if (mCalList.get(position).getPaidLeave() != null){
                holder.pLeaveIv.setVisibility(View.VISIBLE);
            } else {
                holder.pLeaveIv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCalList != null)
        {
            return mCalList.size();
        }
        return 0;
    }


    private class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder

        TextView itemDay;
        ImageView pLeaveIv;

        public DayViewHolder(@NonNull final View itemView) {
            super(itemView);

            initView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (mCalList.get(getAdapterPosition()).isClicked()){ //double selected

                            mCalList.get(getAdapterPosition()).getDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(mCalList.get(getAdapterPosition()).getPaidLeave().second);
                            calendar.get(Calendar.MINUTE);


                            String dateText = mCalList.get(getAdapterPosition()).getMonth() + "월 " +
                                    mCalList.get(getAdapterPosition()).getDate() + "일";
                            redirectDayActivity(context, dateText);
                        } else {
                            for (int i = 0; i < mCalList.size(); i++){
                                if (mCalList.get(i).isClicked()){
                                    mCalList.get(i).setClicked(false);
                                    break;
                                }
                            }
                            mCalList.get(getAdapterPosition()).setClicked(true);
                            selectedDateIndex = getAdapterPosition();
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public void initView(View v){
            itemDay = v.findViewById(R.id.tvDate);
            pLeaveIv = v.findViewById(R.id.ivPaidLeave);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View v){
        }
    }

    private void redirectDayActivity(Context context, String day){
        Intent intent = new Intent(context, DayActivity.class);
        intent.putExtra("Date", day);
        context.startActivity(intent);
    }
}
