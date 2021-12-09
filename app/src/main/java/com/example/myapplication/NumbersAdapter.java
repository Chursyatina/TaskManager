package com.example.myapplication;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Models.Task;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder>{

    private List<Task> tasks;
    private OnClickListener mOnClickListener;
    private OnClickListener cOnClickListener;

    public NumbersAdapter(){}

    public NumbersAdapter(List<Task> newTasks, OnClickListener onClickListener){
        this.tasks = newTasks;
        this.mOnClickListener = onClickListener;

        if(newTasks == null){
            this.tasks = new ArrayList<>();
        }
    }

    public void setTasks(List<Task> Tasks){
        this.tasks = Tasks;
        this.notifyDataSetChanged();
    }

    public void setOnClickOnTaskListener(OnClickListener listener){
        mOnClickListener = listener;
    }

    public void setOnClickOnCheckBoxListener(OnClickListener listener) {
        cOnClickListener = listener;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context  = parent.getContext();
        int layoutIdForListItem = R.layout.task_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        NumberViewHolder viewHolder = new NumberViewHolder(view);


        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        }
        return 0;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        boolean isFirstCheck = true;
        TextView listItemNumberView;
        CheckBox checkBox;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_task);
            checkBox = itemView.findViewById(R.id.checkbox);
            listItemNumberView.setLines(1);
            listItemNumberView.setMaxEms(10);
            listItemNumberView.setEllipsize(TextUtils.TruncateAt.END);

        }

        void bind(Task task){
            itemView.setOnClickListener((v) -> mOnClickListener.onClick(task.getId()));

            checkBox.setOnCheckedChangeListener((v, s) -> {
                if (isFirstCheck) {
                    cOnClickListener.onClick(task.getId());
                }
                isFirstCheck=false;
            });

            listItemNumberView.setText(task.getTaskText());

            if (isFirstCheck) {
                checkBox.setChecked(task.getCompleted());
            }

            isFirstCheck = true;
        }
    }

    public interface OnClickListener {
        void onClick(long id);
    }
}
