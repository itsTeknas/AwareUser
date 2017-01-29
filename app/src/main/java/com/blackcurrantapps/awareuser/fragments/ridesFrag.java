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
import com.blackcurrantapps.awareuser.firebaseModels.Ride;
import com.blackcurrantapps.awareuser.util.firebase.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanket on 29/01/17.
 * Copyright (c) BlackcurrantApps LLP.
 */

public class RidesFrag extends Fragment {

    @BindView(R.id.hint)
    TextView hint;
    private FirebaseRecyclerAdapter<Ride, RidesHolder> mRecyclerViewAdapter;

    MainActivityConnect mainActivityConnect;
    @BindView(R.id.ridesList)
    RecyclerView ridesList;

    String own_mac_id;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnect = (MainActivityConnect) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityConnect.setToolbarTitle("Rides");

        mainActivityConnect.getDatabaseReference().child("users").child(mainActivityConnect.getCell()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                own_mac_id = (String) dataSnapshot.child("mac_id").getValue();

                mainActivityConnect.getDatabaseReference().child("mac_id").child(own_mac_id).child("visits").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> visits = dataSnapshot.getChildren();
                        final HashMap<String, Void> visitsHashMap = new HashMap<String, Void>();
                        for (DataSnapshot visit : visits) {
                            visitsHashMap.put(visit.getKey(), null);
                        }
                        mainActivityConnect.getDatabaseReference().child("rides").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> rides = dataSnapshot.getChildren();

                                Double minWait = 10000.0;
                                DataSnapshot selectedRide = null;

                                for (DataSnapshot ride : rides) {
                                    if (!visitsHashMap.containsKey(ride.getKey())) {
                                        Integer devices_count = Integer.valueOf(String.valueOf(ride.child("current_nearby_devices_count").getValue()));
                                        Integer ride_minutes =  Integer.valueOf(String.valueOf(ride.child("ride_time_minutes").getValue()));
                                        Integer group_size = Integer.valueOf(String.valueOf(ride.child("ride_group_size").getValue()));
                                        Double wait = (double) ( devices_count/ group_size) *ride_minutes;
                                        if (wait < minWait) {
                                            minWait = wait;
                                            selectedRide = ride;
                                        }
                                    }
                                }

                                if (selectedRide != null) {
                                    hint.setText("Head over to "+selectedRide.child("name").getValue()+" for the minimum waiting queue");
                                } else {
                                    hint.setText("You seem to have explored the entire park !\nWe hope you had a great time :)");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_frag, container, false);
        ButterKnife.bind(this, view);

        ridesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ridesList.setHasFixedSize(false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mRef = mainActivityConnect.getDatabaseReference().child("rides");

        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Ride, RidesHolder>(
                Ride.class, R.layout.ride_item, RidesHolder.class, mRef) {

            @Override
            protected void populateViewHolder(RidesHolder viewHolder, Ride ride, int position) {
                viewHolder.rideName.setText(ride.name);
                viewHolder.queueLength.setText(ride.current_nearby_devices_count + " people in queue");
                viewHolder.rideDescription.setText(ride.description);
                viewHolder.waitingTime.setText(String.valueOf((ride.current_nearby_devices_count / ride.ride_group_size) * ride.ride_time_minutes));
                viewHolder.waitingUnits.setText("minutes");
            }
        };
        ridesList.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    public static class RidesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rideName)
        TextView rideName;
        @BindView(R.id.queueLength)
        TextView queueLength;
        @BindView(R.id.waitingTime)
        TextView waitingTime;
        @BindView(R.id.waitingUnits)
        TextView waitingUnits;
        @BindView(R.id.rideDescription)
        TextView rideDescription;

        public RidesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
