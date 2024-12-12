package com.nissisolution.nissibeta.Adapter.Display;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class DisplayHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public DisplayHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }
}
