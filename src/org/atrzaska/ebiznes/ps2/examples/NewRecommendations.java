package org.atrzaska.ebiznes.ps2.examples;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousUserDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;

public class NewRecommendations {
    public static void main(String[] args) throws IOException, TasteException {
        DataModel model = new FileDataModel(new File("data/gr2.csv"));

        PreferenceArray tempPrefs = new GenericUserPreferenceArray(3);
        tempPrefs.setUserID(0, PlusAnonymousUserDataModel.TEMP_USER_ID);
        tempPrefs.setItemID(0, 1);
        tempPrefs.setValue(0, 1.0f);
        tempPrefs.setItemID(1, 2);
        tempPrefs.setValue(1, 3.0f);
        tempPrefs.setItemID(2, 3);
        tempPrefs.setValue(2, 5.0f);

        // prepare new user's preferences
        PlusAnonymousUserDataModel newmodel = new PlusAnonymousUserDataModel(model);
        newmodel.setTempPrefs(tempPrefs);

        // Make a weighted slope one recommender
        Recommender recommender = new SlopeOneRecommender(newmodel);
        Recommender cachingRecommender = new CachingRecommender(recommender);

        // make recommendations for a new user
        System.out.println("Recommended items:" + cachingRecommender.recommend(PlusAnonymousUserDataModel.TEMP_USER_ID, 3));

        newmodel.clearTempPrefs();
    }
}
