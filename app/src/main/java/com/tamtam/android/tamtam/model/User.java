package com.tamtam.android.tamtam.model;
import java.util.List;

/**
 * User model implementation
 */
public class User {
    String mUserId;
    List<String> mSellingThings = null, mInterestedIn = null;

    User(String userId) {
        mUserId = userId;
    }

    User(String userId, List<String> sellingThings) {
        this(userId);
        mSellingThings = sellingThings;
    }

    User(String userId, List<String> interestedIn, List<String> sellingThings) {
        this(userId, sellingThings);
        mInterestedIn = interestedIn;
    }

}