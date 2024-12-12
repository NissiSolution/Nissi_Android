package com.nissisolution.nissibeta.Adapter.Leave;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class LeaveHolder extends RecyclerView.ViewHolder {

    public TextView position, period, duration, leave_type, status;
    public ImageView delete;

    public LeaveHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        period = itemView.findViewById(R.id.period);
        duration = itemView.findViewById(R.id.number_of_days);
        leave_type = itemView.findViewById(R.id.leave_type);
        status = itemView.findViewById(R.id.status);
        delete = itemView.findViewById(R.id.delete);
    }
}
