package com.blackcurrantapps.awareuser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blackcurrantapps.awareuser.R;
import com.blackcurrantapps.awareuser.activities.MainActivityConnect;
import com.blackcurrantapps.awareuser.firebaseModels.User;
import com.blackcurrantapps.awareuser.util.firebase.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanket on 29/01/17.
 * Copyright (c) BlackcurrantApps LLP.
 */

public class Group extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserHolder> mRecyclerViewAdapter;

    MainActivityConnect mainActivityConnect;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnect = (MainActivityConnect) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityConnect.setToolbarTitle("My Group");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mainActivityConnect.getDatabaseReference().child("users").child(mainActivityConnect.getCell()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String groupKey = (String) dataSnapshot.child("group_key").getValue();

                DatabaseReference mRef = mainActivityConnect.getDatabaseReference().child("groups").child(groupKey);

                mRecyclerViewAdapter = new FirebaseRecyclerAdapter<User, UserHolder>(
                        User.class, R.layout.group_item, UserHolder.class, mRef) {

                    @Override
                    protected void populateViewHolder(final UserHolder viewHolder, User user, int position) {
                        viewHolder.personName.setText(user.name);
                        viewHolder.personDesc.setText(user.age);

                        mainActivityConnect.getDatabaseReference().child("mac_id").child(user.mac_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name = (String) dataSnapshot.child("nearest_base_station_name").getValue();
                                Long ts = (Long) dataSnapshot.child("update_timestamp").getValue();

                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                                viewHolder.personDesc.setText("Last seen at "+sdf.format(new Date(ts))+" near "+ name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                };
                recyclerView.setAdapter(mRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.personName)
        TextView personName;
        @BindView(R.id.personDesc)
        TextView personDesc;

        public UserHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
