package com.example.abhishek.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.bakingapp.dataHolder.Steps;
import com.example.abhishek.bakingapp.R;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {


    Context context;
    LayoutInflater inflater;
    List<Steps> stepsList;

    public OnStepClickListener onStepClickListener;

    public StepsAdapter(Context context, List<Steps> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.steps_layout, parent, false);
        StepsViewHolder stepsViewHolder = new StepsViewHolder(view);
        return stepsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, final int position) {

        holder.head.setText(stepsList.get(position).getShortDescription());
        holder.body.setText(stepsList.get(position).getDescription());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStepClickListener.OnStepClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return stepsList.size();
    }


    public interface OnStepClickListener {
        void OnStepClick(int i);
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        TextView head;
        ExpandableTextView body;
        ImageView imageView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.steps_heading);
            body = itemView.findViewById(R.id.steps_body);
            imageView = itemView.findViewById(R.id.arrow_click);
            body.setAnimationDuration(300);
            body.setInterpolator(new AccelerateInterpolator());
            body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    body.toggle();
                }
            });
        }
    }
}
