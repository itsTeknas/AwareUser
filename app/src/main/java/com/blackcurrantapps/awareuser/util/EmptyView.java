package com.blackcurrantapps.awareuser.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blackcurrantapps.awareuser.R;

public class EmptyView extends RecyclerView.ViewHolder {

    public TextView primaryText;
    public TextView secondaryText;

    public EmptyView(View itemView) {
        super(itemView);
        primaryText = (TextView) itemView.findViewById(R.id.nothingPrimary);
        secondaryText = (TextView) itemView.findViewById(R.id.nothingSecondary);
    }
}