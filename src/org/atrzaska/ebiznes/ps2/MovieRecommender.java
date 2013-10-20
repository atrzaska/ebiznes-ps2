package org.atrzaska.ebiznes.ps2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousUserDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.clustering.Model;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.LoglikelihoodSimilarity;

public class MovieRecommender {
    private List<String> movieTitles = new ArrayList<>();
    private DataModel model;
    private ItemSimilarity itemSimilarity;

    public MovieRecommender() throws IOException, TasteException {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("wczytuje tytuly filmow");
        readTitles();
        printTitles();

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("wczytuje baze ocen");

        // utworzenie modelu danych z pliku
        model = new FileDataModel(new File(Config.databaseFile));

        int numUsers = model.getNumUsers();
        int numRatings = model.getNumItems();

        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("baza ocen na podstawie %d uzytkowników i %d ocen.\n", numUsers, numRatings);
        System.out.printf("maksymalna ocena: %.1f\n", model.getMaxPreference());
        System.out.printf("minimalna ocena: %.1f\n", model.getMinPreference());
    }

    public List<String> recommendMovies(int userId, int numMovies) throws TasteException {
        //tworzenie systemu rekomendującego i generowanie rekomendacji
        GenericItemBasedRecommender recom = new GenericItemBasedRecommender(model, itemSimilarity);

        List<RecommendedItem> recommendedItems = recom.recommend(userId, 3);
        List<String> outpuList = new ArrayList<>();

        for (RecommendedItem recommendedItem : recommendedItems) {
            String titleString = movieTitles.get((int)recommendedItem.getItemID());
            outpuList.add(titleString);
        }

        return outpuList;
    }

    public List<String> recommendMoviesUser12() throws TasteException {

        PreferenceArray tempPrefs = new GenericUserPreferenceArray(17);
        tempPrefs.setUserID(0, PlusAnonymousUserDataModel.TEMP_USER_ID);

        tempPrefs.setItemID(0, 1);
        tempPrefs.setItemID(1, 2);
        tempPrefs.setItemID(2, 3);
        tempPrefs.setItemID(3, 4);
        tempPrefs.setItemID(4, 6);
        tempPrefs.setItemID(5, 7);
        tempPrefs.setItemID(6, 8);
        tempPrefs.setItemID(7, 10);
        tempPrefs.setItemID(8, 11);
        tempPrefs.setItemID(9, 13);
        tempPrefs.setItemID(10, 14);
        tempPrefs.setItemID(11, 19);
        tempPrefs.setItemID(12, 20);
        tempPrefs.setItemID(13, 21);
        tempPrefs.setItemID(14, 22);
        tempPrefs.setItemID(15, 23);
        tempPrefs.setItemID(16, 24);

        tempPrefs.setValue(0, 2.0f);
        tempPrefs.setValue(1, 3.0f);
        tempPrefs.setValue(2, 4.0f);
        tempPrefs.setValue(3, 4.0f);
        tempPrefs.setValue(4, 2.0f);
        tempPrefs.setValue(5, 4.0f);
        tempPrefs.setValue(6, 5.0f);
        tempPrefs.setValue(7, 4.0f);
        tempPrefs.setValue(8, 4.0f);
        tempPrefs.setValue(9, 5.0f);
        tempPrefs.setValue(10, 3.0f);
        tempPrefs.setValue(11, 5.0f);
        tempPrefs.setValue(12, 4.0f);
        tempPrefs.setValue(13, 4.0f);
        tempPrefs.setValue(14, 3.0f);
        tempPrefs.setValue(15, 5.0f);
        tempPrefs.setValue(16, 5.0f);

        // prepare new user's preferences
        PlusAnonymousUserDataModel tempModel = new PlusAnonymousUserDataModel(model);
        tempModel.setTempPrefs(tempPrefs);

        // Make a weighted slope one recommender
        Recommender slopeOneRecommender = new SlopeOneRecommender(tempModel);
        Recommender cachingRecommender = new CachingRecommender(slopeOneRecommender);


        // make recommendations for a new user
        List<RecommendedItem> recommendedItems = cachingRecommender.recommend(PlusAnonymousUserDataModel.TEMP_USER_ID, 3);
//      System.out.println("Recommended items:" + recommender.recommend(PlusAnonymousUserDataModel.TEMP_USER_ID, 3));

        List<String> outpuList = new ArrayList<>();

        for (RecommendedItem recommendedItem : recommendedItems) {
            String titleString = movieTitles.get((int)recommendedItem.getItemID());
            outpuList.add(titleString);
        }

        // clear preferences
        tempModel.clearTempPrefs();

        return outpuList;
    }

    protected void readTitles() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Config.titlesFile));

        try {
            String line = br.readLine();

            while (line != null) {
                movieTitles.add(line);

                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }

    public void setItemSimilarity(ItemSimilarity itemSimilarity) {
        this.itemSimilarity = itemSimilarity;
    }

    public DataModel getModel() {
        return model;
    }

    private void printTitles() {
        for (int i = 0; i < movieTitles.size(); i++) {
            System.out.printf("%d. %s\n", i+1, movieTitles.get(i));
        }
    }

    public void printUserDetails(int userId) throws TasteException {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("filmy ocenione przez uzytkownika %d:\n", userId);
//        System.out.println(model.getItemIDsFromUser(userId));
        long[] userItems = model.getItemIDsFromUser(userId).toArray();

        PreferenceArray userRatings = model.getPreferencesFromUser(userId);
//        System.out.println(userRatings);

        for (int i = 0; i < userItems.length; i++) {
            int movieId = (int)userItems[i];

            System.out.printf("%d. %s, ocena: %.1f\n",
                    movieId,
                    movieTitles.get(movieId-1),
                    userRatings.getValue(i));
        }
    }

    public void printRecommendations(int userId, int numMovies) throws TasteException {
        System.out.printf("rekomendacje dla uzytkownika %d: ", userId);
        List<String> recommendationsList = this.recommendMovies(userId, numMovies);

        for (String string : recommendationsList) {
            System.out.print(string + " ");
        }

        System.out.println();
    }

    public void printRecommendations(List<String> recommendationsList) {
        System.out.printf("rekomendacje dla uzytkownika tymczasowego: ");

        for (String string : recommendationsList) {
            System.out.print(string + " ");
        }

        System.out.println();
    }
}
