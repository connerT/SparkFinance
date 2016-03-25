package com.sparkfinance.service.impl;

import com.sparkfinance.dao.MessageDao;
import com.sparkfinance.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiniTwitService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;
}
