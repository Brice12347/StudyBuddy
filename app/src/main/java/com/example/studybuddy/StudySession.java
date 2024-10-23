package com.example.studybuddy;
import java.util.Date;
import java.text.SimpleDateFormat;

public class StudySession {
    public String sessionID;
    private Date timestamp;
    public String topic;

    public StudySession(String sessionID, String topic, Date timestamp) {
        this.sessionID = sessionID;
        this.topic = topic;
        this.timestamp = timestamp;
    }

    public String viewSessionDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "Session ID: " + sessionID + "\n" +
                "Topic: " + topic + "\n" +
                "Time: " + dateFormat.format(timestamp);  // Corrected method call
    }

    public void editSessionDetails(String topic, Date newDate) {
        if (topic != null && !topic.isEmpty()) {
            this.topic = topic;
        }
        if (newDate != null) {
            this.timestamp = newDate;
        }
    }

    public String getSessionID() {
        return sessionID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTopic() {
        return topic;
    }
}
