package com.nissisolution.nissibeta.Adapter.GuestHouseAvailability;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Classes.GuestHouseAvailability;
import com.nissisolution.nissibeta.R;

import java.util.List;

public class GuestHouseAvailabilityAdapter extends RecyclerView.Adapter<GuestHouseAvailabilityHolder> {

    private Context context;
    private List<GuestHouseAvailability> guestHouseAvailabilityList;

    public GuestHouseAvailabilityAdapter(Context context, List<GuestHouseAvailability> guestHouseAvailabilityList) {
        this.context = context;
        this.guestHouseAvailabilityList = guestHouseAvailabilityList;
    }

    @NonNull
    @Override
    public GuestHouseAvailabilityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_availability, parent, false);
        return new GuestHouseAvailabilityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestHouseAvailabilityHolder holder, int position) {
        GuestHouseAvailability availability = guestHouseAvailabilityList.get(position);

        switch (availability.count) {
            case 1:
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.GONE);
                holder.layout4.setVisibility(View.GONE);

                holder.layout1Dummy.setVisibility(View.GONE);
                holder.layout2Dummy.setVisibility(View.VISIBLE);
                holder.layout3Dummy.setVisibility(View.VISIBLE);
                holder.layout4Dummy.setVisibility(View.VISIBLE);

                break;
            case 2:
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3.setVisibility(View.GONE);
                holder.layout4.setVisibility(View.GONE);

                holder.layout1Dummy.setVisibility(View.GONE);
                holder.layout2Dummy.setVisibility(View.GONE);
                holder.layout3Dummy.setVisibility(View.VISIBLE);
                holder.layout4Dummy.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3.setVisibility(View.VISIBLE);
                holder.layout4.setVisibility(View.GONE);

                holder.layout1Dummy.setVisibility(View.GONE);
                holder.layout2Dummy.setVisibility(View.GONE);
                holder.layout3Dummy.setVisibility(View.GONE);
                holder.layout4Dummy.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3.setVisibility(View.VISIBLE);
                holder.layout4.setVisibility(View.VISIBLE);

                holder.layout1Dummy.setVisibility(View.GONE);
                holder.layout2Dummy.setVisibility(View.GONE);
                holder.layout3Dummy.setVisibility(View.GONE);
                holder.layout4Dummy.setVisibility(View.GONE);
                break;
            default:
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.GONE);
                holder.layout4.setVisibility(View.GONE);

                holder.layout1Dummy.setVisibility(View.VISIBLE);
                holder.layout2Dummy.setVisibility(View.VISIBLE);
                holder.layout3Dummy.setVisibility(View.VISIBLE);
                holder.layout4Dummy.setVisibility(View.VISIBLE);
                break;
        }

        int count = position * 4;

        holder.text1.setText(getDouble(count + 1));
        holder.text2.setText(getDouble(count + 2));
        holder.text3.setText(getDouble(count + 3));
        holder.text4.setText(getDouble(count + 4));
        
        setBackground(holder.layout1, availability.a1);
        setBackground(holder.layout2, availability.a2);
        setBackground(holder.layout3, availability.a3);
        setBackground(holder.layout4, availability.a4);
    }

    private String getDouble(int count) {
        if (count < 10) {
            return "0" + count;
        } else {
            return String.valueOf(count);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setBackground(RelativeLayout layout, int availability) {
        switch (availability) {
            case 0:
                layout.setBackground(context.getDrawable(R.drawable.background_unknown_guest));
                break;
            case 1:
                layout.setBackground(context.getDrawable(R.drawable.background_available_guest));
                break;
            case 2:
                layout.setBackground(context.getDrawable(R.drawable.background_unavailable_guest));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return guestHouseAvailabilityList.size();
    }
}
