package com.nissisolution.nissibeta.Adapter.PunchedLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.Activity.Locations.UniqueLocationActivity;
import com.nissisolution.nissibeta.Classes.CheckedInfo;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.text.SimpleDateFormat;
import java.util.List;

public class PunchedLocationAdapter extends RecyclerView.Adapter<PunchedLocationHolder> {

    private Context context;
    private List<CheckedInfo> checked_list;
    private final int KEY_PUNCHED_IN = 1;
    private final int KEY_PUNCHED_OUT = 2;

    public PunchedLocationAdapter(Context context, List<CheckedInfo> checked_list) {
        this.context = context;
        this.checked_list = checked_list;
    }

    @NonNull
    @Override
    public PunchedLocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == KEY_PUNCHED_IN) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_punched_in_layout, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_punched_out_layout, parent, false);
        }

        return new PunchedLocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PunchedLocationHolder holder, int position) {
        CheckedInfo info = checked_list.get(position);
        holder.staff_name.setText(info.staff_name);
        holder.time.setText(get_date(info.date));
        if (info.latitude != 0 && info.longitude != 0) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UniqueLocationActivity.class);
                intent.putExtra(Constants.KEY_DATA, info.data);
                context.startActivity(intent);
            });
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date(String datetime) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
        try {
            return format2.format(format1.parse(datetime));
        } catch (Exception ignored) {
            return "Unknown";
        }
    }

    @Override
    public int getItemCount() {
        return checked_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        CheckedInfo info = checked_list.get(position);
        return info.type;
    }
}
