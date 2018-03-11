package com.whitewalkers.smartnotificationsmanager;

import android.util.Log;

/**
 * Created by jijai on 25-Jul-17.
 */

public class NoobSpamClassifier implements ISpamClassifier {

    private final String[] trustedApps = {
        "whatsapp",
        "facebook"};

    private final String[] spamWords = {
        "%",
        "sale",
        "off",
        "discount",
        "offer"
    };

    @Override
    public boolean isSpamNotification(SNMNotification notification) {
        Log.e("jj", "notification info " + notification.getTitle() + " " + notification.getDescription());
        String title = notification.getTitle();
        String description = notification.getDescription();
        String packageName = notification.getPackageName();

        if (this.listContains(this.trustedApps, packageName)){
            return false;
        }

        if (this.listContains(this.spamWords, title) || this.listContains(this.spamWords, description)){
            return true;
        }
        
        return false;
    }

    private boolean listContains(String[] arr, String key) {
        key = key.toLowerCase();
        for (String arrElement : arr) {
            if (key.contains(arrElement.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
