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
public class User {

    public String age;
    public String name;

    public User() {
    }

    public User(String age, String name) {

        this.age = age;
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("age", age);
        result.put("name", name);
        return result;
    }
}
