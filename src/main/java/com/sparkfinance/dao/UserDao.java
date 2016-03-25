package com.sparkfinance.dao;

import com.sparkfinance.model.User;

/**
 * Created by conner.tolley on 3/23/16.
 */
public interface UserDao {

    User getUserbyUsername(String username);

    void insertFollower(User follower, User followee);

    void deleteFollower(User follower, User followee);

    boolean isUserFollower(User follower, User followee);

    void registerUser(User user);

}
