package com.nissisolution.nissibeta.Adapter.SupportSos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class SupportSosHolder extends RecyclerView.ViewHolder {

    public TextView title, message, author;

    public SupportSosHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        message = itemView.findViewById(R.id.content);
        author = itemView.findViewById(R.id.author);

    }
}
