package com.nissisolution.nissibeta.Adapter.PunchedLocation;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class PunchedLocationHolder extends RecyclerView.ViewHolder {

    public TextView staff_name, time;

    public PunchedLocationHolder(@NonNull View itemView) {
        super(itemView);
        staff_name = itemView.findViewById(R.id.staff_name);
        time = itemView.findViewById(R.id.time);
    }
}
