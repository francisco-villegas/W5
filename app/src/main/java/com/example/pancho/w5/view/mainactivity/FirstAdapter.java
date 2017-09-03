package com.example.pancho.w5.view.mainactivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.pancho.w5.R;
import com.example.pancho.w5.model.HourlyForecastOrdered;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 8/27/2017.
 */

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder>{
    private static final String TAG = "Adapter";
    List<HourlyForecastOrdered> hourlyForecastOrdered;
    Context context;
    private int lastPosition = -1;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    public FirstAdapter(List<HourlyForecastOrdered> hourlyForecastOrdered) {
        this.hourlyForecastOrdered = hourlyForecastOrdered;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HourlyForecastOrdered item = hourlyForecastOrdered.get(position);

        holder.tvLabel.setText(item.getLabel());

        layoutManager = new GridLayoutManager(context,4);
        itemAnimator = new DefaultItemAnimator();
        holder.sub_recycler.setLayoutManager(layoutManager);
        holder.sub_recycler.setItemAnimator(itemAnimator);

        SecondAdapter secondAdapter = new SecondAdapter(item.getHourlyForecastOrdered());
        holder.sub_recycler.setAdapter(secondAdapter);
        secondAdapter.notifyDataSetChanged();
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return hourlyForecastOrdered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tvLabel)
        TextView tvLabel;

        @Nullable
        @BindView(R.id.sub_recycler)
        RecyclerView sub_recycler;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
