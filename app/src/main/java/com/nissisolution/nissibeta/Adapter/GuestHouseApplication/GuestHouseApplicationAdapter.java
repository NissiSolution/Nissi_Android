package com.nissisolution.nissibeta.Adapter.GuestHouseApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Classes.GuestHouseApplication;
import com.nissisolution.nissibeta.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class GuestHouseApplicationAdapter extends RecyclerView.Adapter<GuestHouseApplicationHolder> {

    private Context context;
    private List<GuestHouseApplication> guestHouseApplicationList;

    public GuestHouseApplicationAdapter(Context context, List<GuestHouseApplication> guestHouseApplicationList) {
        this.context = context;
        this.guestHouseApplicationList = guestHouseApplicationList;
    }

    @NonNull
    @Override
    public GuestHouseApplicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_guest_house_application, parent, false);
        return new GuestHouseApplicationHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GuestHouseApplicationHolder holder, int position) {
        GuestHouseApplication application = guestHouseApplicationList.get(position);
        holder.position.setText(getPosition(position + 1));
        holder.employeeName.setText(application.employeeName);
        holder.employeeId.setText(application.employeeId);
        holder.requestedOn.setText(getDateTime(application.requestedOn));
        holder.period.setText(getDateTime(application.startDate) + " to " + getDateTime(application.endDate));
    }

    private String getPosition(int position) {
        if (position < 10) {
            return "00" + position + ".";
        } else if (position < 100) {
            return "0" + position + ".";
        } else {
            return position + ".";
        }
    }

    private String getDateTime(long timestamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return format.format(timestamp);
    }

    @Override
    public int getItemCount() {
        return guestHouseApplicationList.size();
    }
}
