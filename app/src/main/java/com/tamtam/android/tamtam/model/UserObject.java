/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tamtam.android.tamtam.model;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.validation.Validator;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by antoninpa on 09/01/17.
 * UserObject model implementation
 */
public class UserObject{
    private final String userId;
    private final Collection<String> sellingThings,
                              interestedIn;


    public UserObject(String userId, Collection<String> interestedIn, Collection<String> sellingThings) {
        // we check if the given parameters are valid :
        if (userId == null || userId.isEmpty() )
            throw new IllegalArgumentException("userId must be non null, non empty");

        this.userId = userId;
        this.sellingThings = sellingThings;
        this.interestedIn = interestedIn;
    }

    public Collection<String> getInterestedIn() { return interestedIn; }
    public Collection<String> getSellingThings() { return sellingThings; }
    public String getUserId() { return userId; }

    @Override
    public String toString() {
        return "UserObject{" +
                "userId='" + userId + '\'' +
                ", sellingThings=" + sellingThings +
                ", interestedIn=" + interestedIn +
                '}';
    }

    // todo : check if equality valid on generic hashmap (may use object's equal method instead)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // cast if o is a UserObject
        UserObject that = (UserObject) o;

        if (!userId.equals(that.userId)) return false;
        if (sellingThings != null ?
                !(new HashSet<String>(sellingThings)).equals(new HashSet<String>(that.sellingThings)) :
                that.sellingThings != null)
            return false;
        return interestedIn != null ?
                (new HashSet<String>(interestedIn)).equals(new HashSet<String>(that.interestedIn)) :
                that.interestedIn == null;

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        //result = 31 * result + (sellingThings != null ? sellingThings.hashCode() : 0);
        //result = 31 * result + (interestedIn != null ? interestedIn.hashCode() : 0);
        return result;
    }
}