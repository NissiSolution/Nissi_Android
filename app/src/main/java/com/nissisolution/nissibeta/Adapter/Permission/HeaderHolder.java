package com.nissisolution.nissibeta.Adapter.Permission;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class HeaderHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public HeaderHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

}
