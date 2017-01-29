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


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * Created by fcng1847 on 12/01/17.
 */
public class UserObjectTest {


    UserObject userObject = null;
    Set<String> interestSet = new HashSet(Arrays.asList("thing1","thing2"));
    Set<String> sellingSet = new HashSet(Arrays.asList("thing3","thing4"));


    @Before
    @Test
    public void constructor() throws Exception {
        userObject = new UserObject("user1", interestSet, sellingSet);
        assertSame(UserObject.class, userObject.getClass());
        assertNotNull(userObject);
    }

    @Test
    public void getInterestedIn() throws Exception {
        assertEquals(interestSet, userObject.getInterestedIn());
    }

    @Test
    public void getSellingThings() throws Exception {
        assertEquals(sellingSet, userObject.getSellingThings());
    }

    @Test
    public void getUserId() throws Exception {
        assertEquals("user1", userObject.getUserId());
    }

    @Test
    public void UserToString() throws Exception {
        assertTrue(userObject.toString().contains("userId"));
        assertTrue(userObject.toString().contains("interestedIn"));
        assertTrue(userObject.toString().contains("sellingThings"));

    }

}