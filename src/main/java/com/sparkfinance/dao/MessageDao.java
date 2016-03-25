package com.sparkfinance.dao;

import com.sparkfinance.model.Message;
import com.sparkfinance.model.User;

import java.util.List;

public interface MessageDao {
    List<Message> getUserTimeLineMessages(User user);

    List<Message> getUserFullTimeLineMessages(User user);

    List<Message> getPublicTimeLineMessages();

    void insertMessage(Message m);
}
