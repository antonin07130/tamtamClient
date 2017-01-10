package com.tamtam.android.tamtam.model;
import java.util.List;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by antoninpa on 09/01/17.
 * UserObject model implementation
 */
public class UserObject{
    private final String userId;
    private final Set<String> sellingThings,
                              interestedIn;


    public UserObject(String userId, Set<String> interestedIn, Set<String> sellingThings) {
        this.userId = userId;
        this.sellingThings = sellingThings;
        this.interestedIn = interestedIn;
    }

    public Set<String> getInterestedIn() { return interestedIn; }
    public Set<String> getSellingThings() { return sellingThings; }
    public String getUserId() { return userId; }

    @Override
    public String toString() {
        return super.toString() + "( userId : " + this.userId +
                                  ", sellingThings : " + this.sellingThings +
                                  ", interestedIn : " + this.interestedIn + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserObject that = (UserObject) o;

        if (!userId.equals(that.userId)) return false;
        if (sellingThings != null ? !sellingThings.equals(that.sellingThings) : that.sellingThings != null)
            return false;
        return interestedIn != null ? interestedIn.equals(that.interestedIn) : that.interestedIn == null;

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + (sellingThings != null ? sellingThings.hashCode() : 0);
        result = 31 * result + (interestedIn != null ? interestedIn.hashCode() : 0);
        return result;
    }
}