package com.whitewalkers.smartnotificationsmanager;

public interface ISpamClassifier {

    boolean isSpamNotification(SNMNotification notification);
}
