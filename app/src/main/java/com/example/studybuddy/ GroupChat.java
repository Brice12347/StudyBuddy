package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class GroupChat{

    public String chatID;
    List<messages>

    public Groupchat(String chatID){
        this.chatID = chatID;
        this.messages = new ArrayList<>();
    }

    //this method is necessary in order to
    //create and send messages to a groupchat
    public void sendMessage(User user, String content){
        if(user != null && content != null && !content.isEmpty()){
            Message newMessage = new Message(user, content);
            messages.add(newNewMessage);
        }
    }
    // this method is used in order to
    //retrieve messages from the groupchat
    public List<Message> getMessage(){
        return new ArrayList<>(messages);
    }

    //get the chatID to find corresponding
    //messages
    public String getChatID(){
        return chatID;
    }
}