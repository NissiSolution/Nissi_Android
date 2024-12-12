package com.nissisolution.nissibeta.Classes;

public class NotificationClasses {
    public String title, notification;
    public int type, keyId, sent, viewed;
    public long timestamp;

    public NotificationClasses(String title, String notification, int type, int keyId,
                               long timestamp, int sent, int viewed) {
        this.title = title;
        this.notification = notification;
        this.type = type;
        this.keyId = keyId;
        this.timestamp = timestamp;
        this.sent = sent;
        this.viewed = viewed;
    }
}
