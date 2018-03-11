package com.whitewalkers.smartnotificationsmanager;

import android.content.Context;
import android.graphics.Bitmap;

public class SNMNotification {

    private int _id;
    private String _title;
    private String _description;
    private String _packageName;
    private String _appName;
    private String _tag;
    private long _postTime;
    private int _iconId;
    private int _blocked;

    public SNMNotification(){}
    public SNMNotification(int id,
                           String tag,
                           long postTime,
                           String packageName,
                           String appName,
                           String title,
                           String description,
                           int iconId,
                           int blocked){
        this._id = id;
        this._title = title;
        this._description = description;
        this._packageName = packageName;
        this._appName = appName;
        this._tag = tag;
        this._postTime = postTime;
        this._iconId = iconId;
        this._blocked = blocked;
    }

    public int getId(){
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getBlocked(){
        return this._blocked;
    }

    public void setBlocked(int blocked) {
        this._blocked= blocked;
    }

    public String getTitle(){
        return this._title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getDescription(){
        return this._description;
    }

    public void setDescription(String description){
        this._description = description;
    }

    public String getPackageName() {
        return this._packageName;
    }

    public void setPackageName(String packageName){
        this._packageName = packageName;
    }

    public String getAppName() {
        return this._appName;
    }

    public void setAppName(String appName){
        this._appName = appName;
    }

    public String getTag(){
        return this._tag;
    }

    public void setTag(String tag){
        this._tag = tag;
    }

    public long getPostTime(){
        return this._postTime;
    }

    public void setPostTime(long postTime) {
        this._postTime = postTime;
    }

    public int getIconid(){
        return this._iconId;
    }

    public void setIconid(int iconId){
        this._iconId = iconId;
    }
}
