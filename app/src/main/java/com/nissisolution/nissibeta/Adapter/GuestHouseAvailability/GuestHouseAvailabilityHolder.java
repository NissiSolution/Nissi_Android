package com.nissisolution.nissibeta.Adapter.GuestHouseAvailability;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nissisolution.nissibeta.R;

public class GuestHouseAvailabilityHolder extends RecyclerView.ViewHolder {

    public Space layout1Dummy, layout2Dummy, layout3Dummy, layout4Dummy;
    public RelativeLayout layout1, layout2, layout3, layout4;
    public TextView text1, text2, text3, text4;

    public GuestHouseAvailabilityHolder(@NonNull View itemView) {
        super(itemView);

        layout1Dummy = itemView.findViewById(R.id.layout1Dummy);
        layout2Dummy = itemView.findViewById(R.id.layout2Dummy);
        layout3Dummy = itemView.findViewById(R.id.layout3Dummy);
        layout4Dummy = itemView.findViewById(R.id.layout4Dummy);

        layout1 = itemView.findViewById(R.id.layout1);
        layout2 = itemView.findViewById(R.id.layout2);
        layout3 = itemView.findViewById(R.id.layout3);
        layout4 = itemView.findViewById(R.id.layout4);

        text1 = itemView.findViewById(R.id.text1);
        text2 = itemView.findViewById(R.id.text2);
        text3 = itemView.findViewById(R.id.text3);
        text4 = itemView.findViewById(R.id.text4);
    }
}
