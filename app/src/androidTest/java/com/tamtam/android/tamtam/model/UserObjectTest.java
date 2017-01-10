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
    Set<String> interestSet = new HashSet(Arrays.asList("thing1,thing2"));
    Set<String> sellingSet = new HashSet(Arrays.asList("thing3,thing4"));


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