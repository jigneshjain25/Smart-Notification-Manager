package com.whitewalkers.smartnotificationsmanager;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by jijai on 29-Jul-17.
 */

public class FileUtils {
    private static final String appFolderName = "SmartNotificationBlocker";

    public static void writeToFile(Context context, String data, String fileName) {
        try {
            File folder = createAppFolder(context);
            File file = new File(folder, fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
            Toast.makeText(context, file.getAbsolutePath() + " saved.", Toast.LENGTH_LONG).show();
            Toast.makeText(context, file.getAbsolutePath() + " saved.", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.e("JJ", "File write failed: " + e.toString());
        }
    }

    private static File createAppFolder(Context context){
        File f = new File(Environment.getExternalStorageDirectory(), appFolderName);
        if (!f.exists()) {
            f.mkdirs();
        }

        return f;
    }
}
