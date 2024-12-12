package com.nissisolution.nissibeta.Adapter.Permission;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class DataHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public CheckBox permission;
    public LinearLayout layout;

    public DataHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        permission = itemView.findViewById(R.id.permission);
        layout = itemView.findViewById(R.id.layout);
    }
}
