package com.nissisolution.nissibeta.Adapter.Display;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.Arrays;
import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayHolder> {

    public Context context;
    public List<String> data;

    public DisplayAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public DisplayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text, parent, false);
        return new DisplayHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull DisplayHolder holder, int position) {
        List<String> data_1 = Arrays.asList(data.get(position).split(Constants.KEY_SPLITTER_2));
        holder.message.setText(data.get(position));
        switch (data_1.get(1)) {
            case "W":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_present));
                holder.message.setText("Worked on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.nissi_blue));
                break;
            case "CO":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_present));
                holder.message.setText("Compensation Off on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.nissi_blue));
                break;
            case "LH":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_present));
                holder.message.setText("Local Holiday on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.nissi_blue));
                break;
            case "HO":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_holiday));
                holder.message.setText("Holiday on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case "SL":
            case "SI":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Sick Leave on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
            case "CL":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Casual Leave on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
            case "EL":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Earned Leave on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
            case "PL":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Paternity Leave on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
            case "ML":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Marriage Leave on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
            case "LOP":
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_red));
                holder.message.setText("Loss of Pay on " + data_1.get(0));
                holder.message.setTextColor(context.getResources().getColor(R.color.white));
                break;
            default:
                holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.background_current_date));
                holder.message.setText("Leave on " + data_1.get(0) + data_1.get(1));
                holder.message.setTextColor(context.getResources().getColor(R.color.signature_blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
