package com.nissisolution.nissibeta.Adapter.Missed_Punch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class MissedPunchHolder extends RecyclerView.ViewHolder {
    public TextView position, date, working_hours, status;
    public ImageView delete;
    public MissedPunchHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        date = itemView.findViewById(R.id.date);
        working_hours = itemView.findViewById(R.id.working_hours);
        status = itemView.findViewById(R.id.status);
        delete = itemView.findViewById(R.id.delete);
    }
}
