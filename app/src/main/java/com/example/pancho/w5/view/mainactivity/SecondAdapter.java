package com.example.pancho.w5.view.mainactivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pancho.w5.R;
import com.example.pancho.w5.model.HourlyNeeded;
import com.example.pancho.w5.util.CONSTANTS;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 8/27/2017.
 */

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.ViewHolder>{
    private static final String TAG = "Adapter";
    private final int minp;
    private final int maxp;
    List<HourlyNeeded> hourlyNeeded;
    Context context;
    private int lastPosition = -1;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    private String unit;

    public SecondAdapter(List<HourlyNeeded> hourlyNeeded, int minp, int maxp) {
        this.hourlyNeeded = hourlyNeeded;
        this.minp = minp;
        this.maxp = maxp;
    }

    public void setUnits(String unit){
        this.unit = unit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_sub, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HourlyNeeded item = hourlyNeeded.get(position);

        holder.tvhour.setText(item.getHour());
        if (unit.equals("Celsius")) {
            holder.tvdegree_sub.setText(item.getCelsius());
        } else {
            holder.tvdegree_sub.setText(item.getFahrenheit());
        }
        holder.imageView.setIconResource(context.getResources().getString(Integer.parseInt(item.getUrl())));
        if (position == minp && position == maxp){
            if (position == minp) {
                holder.imageView.setIconColor(ContextCompat.getColor(context, CONSTANTS.min_color));
            } else if (position == maxp) {
                holder.imageView.setIconColor(ContextCompat.getColor(context, CONSTANTS.max_color));
            }
        }
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
        return hourlyNeeded.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tvhour)
        TextView tvhour;

        @Nullable
        @BindView(R.id.tvdegree_sub)
        TextView tvdegree_sub;

        @Nullable
        @BindView(R.id.imageView)
        WeatherIconView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
