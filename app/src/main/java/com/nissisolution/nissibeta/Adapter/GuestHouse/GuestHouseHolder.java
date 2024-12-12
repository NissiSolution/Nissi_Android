package com.nissisolution.nissibeta.Adapter.GuestHouse;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.nissisolution.nissibeta.R;

public class GuestHouseHolder extends RecyclerView.ViewHolder {

    public ShapeableImageView l_image, r_image;
    public TextView l_text, r_text;
    public String l_link, r_link;

    public GuestHouseHolder(@NonNull View itemView) {
        super(itemView);

        l_image = itemView.findViewById(R.id.l_image);
        r_image = itemView.findViewById(R.id.r_image);
        l_text = itemView.findViewById(R.id.l_text);
        r_text = itemView.findViewById(R.id.r_text);

    }
}
