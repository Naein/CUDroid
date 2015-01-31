package com.nashesz.cudroid.DataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 31/01/2015.
 */
public class Topic {
    private String _name;
    private List<Message> _messageList;

    public Topic() {
        _messageList = new ArrayList<Message>();
    }
}
