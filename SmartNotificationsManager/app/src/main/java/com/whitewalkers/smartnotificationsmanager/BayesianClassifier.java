package com.whitewalkers.smartnotificationsmanager;

import android.content.Context;
import android.util.Log;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.concurrent.Exchanger;

/**
 * Created by gdesh on 7/26/2017.
 */

public class BayesianClassifier implements ISpamClassifier {
    Classifier<String, String> bayesClassifier = null;

    final String promotionalCategory = new String("promotional");
    final String importantCategory = new String("important");

    public BayesianClassifier(Context context) throws Exception {
        if (this.bayesClassifier == null) {
            try {
                this.bayesClassifier = (BayesClassifier) this.readModel(context.getAssets().open("BayesClassifier.model"));
            }
            catch (Exception e) {
                throw e;
            }
        }
    }

    public boolean isSpamNotification(SNMNotification notification) {
        String title = notification.getTitle();
        String description = notification.getDescription();
        String packageName = notification.getPackageName();

        String titleCategory = this.bayesClassifier.classify(Arrays.asList(title.toLowerCase().split("\\s"))).getCategory();
        String descriptionCategory = this.bayesClassifier.classify(Arrays.asList(description.toLowerCase().split("\\s"))).getCategory();
        /*Log.e("gd", title);
        Log.e("gd", titleCategory);
        Log.e("gd", description);
        Log.e("gd", descriptionCategory);*/
        if (descriptionCategory.equalsIgnoreCase(promotionalCategory)){
            return true;
        }

        return false;
    }

    private static Classifier readModel(InputStream is) {
        BayesClassifier bayes = null;
        try {
            //FileInputStream fi = new FileInputStream(new File(filePath));
            ObjectInputStream oi = new ObjectInputStream(is);
            bayes = (BayesClassifier)oi.readObject();
            oi.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (Exception e) {}
        }

        return bayes;
    }
}
