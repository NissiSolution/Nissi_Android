package com.nissisolution.nissibeta.Adapter.CompOffHoliday;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class CompOffHolidayHolder extends RecyclerView.ViewHolder {
    public TextView position, date, request_type, status;
    public ImageView delete;
    public CompOffHolidayHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        date = itemView.findViewById(R.id.date);
        request_type = itemView.findViewById(R.id.request_type);
        status = itemView.findViewById(R.id.status);
        delete = itemView.findViewById(R.id.delete);
    }
}
