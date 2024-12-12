package com.nissisolution.nissibeta.Adapter.Text;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class TextHolder extends RecyclerView.ViewHolder {

    public TextView position, leave_type, total_days, remaining_days;

    public TextHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position);
        leave_type = itemView.findViewById(R.id.leave_type);
        total_days = itemView.findViewById(R.id.total_leave);
        remaining_days = itemView.findViewById(R.id.remaining_days);
    }
}
