package com.blackcurrantapps.awareuser.firebaseModels;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sanket on 29/01/17.
 * Copyright (c) BlackcurrantApps LLP.
 */
@IgnoreExtraProperties
public class Ride {

    public int current_nearby_devices_count;
    public String description;
    public String name;
    public int ride_group_size;
    public int ride_time_minutes;

    public Ride(int current_nearby_devices_count, String description, String name, int ride_group_size, int ride_time_minutes) {
        this.current_nearby_devices_count = current_nearby_devices_count;
        this.description = description;
        this.name = name;
        this.ride_group_size = ride_group_size;
        this.ride_time_minutes = ride_time_minutes;
    }

    public Ride() {
    }

    public int getCurrent_nearby_devices_count() {
        return current_nearby_devices_count;
    }

    public void setCurrent_nearby_devices_count(int current_nearby_devices_count) {
        this.current_nearby_devices_count = current_nearby_devices_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRide_group_size() {
        return ride_group_size;
    }

    public void setRide_group_size(int ride_group_size) {
        this.ride_group_size = ride_group_size;
    }

    public int getRide_time_minutes() {
        return ride_time_minutes;
    }

    public void setRide_time_minutes(int ride_time_minutes) {
        this.ride_time_minutes = ride_time_minutes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("current_nearby_devices_count", current_nearby_devices_count);
        result.put("description", description);
        result.put("name", name);
        result.put("ride_group_size", ride_group_size);
        result.put("ride_time_minutes", ride_time_minutes);
        return result;
    }
}
