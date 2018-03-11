package com.whitewalkers.smartnotificationsmanager;

/**
 * Created by gdesh on 7/26/2017.
 */

public class AppSetting {
    String _packageName;
    boolean _alwaysBlock;
    boolean _alwaysShow;

    public AppSetting(){}
    public AppSetting(String packageName,
                      int alwaysBlock,
                      int alwaysShow){
        this._packageName = packageName;
        this._alwaysBlock = (alwaysBlock == 1);
        this._alwaysShow = (alwaysShow == 1);
    }

    public String getPackageName() {
        return this._packageName;
    }

    public void setPackageName(String packageName){
        this._packageName = packageName;
    }

    public boolean isAlwaysBlock() {
        return this._alwaysBlock;
    }

    public void setAlwaysBlock(boolean blocked){ this._alwaysBlock = blocked; }

    public boolean isAlwaysShow() {
        return this._alwaysShow;
    }

    public void setAlwaysShow(boolean show){
        this._alwaysShow = show;
    }
}
