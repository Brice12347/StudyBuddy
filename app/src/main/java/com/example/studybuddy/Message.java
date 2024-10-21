package com.example.studybuddy;

public class Message{
    public User sender;
    public String content;
    private DateTime timestamp;

    public Message(String messageID, user sender, String content){
        this.sender = sender;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    //get the the sender
   public User getSender(){
        return sender;
   }
   // get the content that will be in
   // the message
   public String getContent(){
        return content;
   }

   //get the current time
   public long getTimeStamp(){
        return timeStamp;
   }
}