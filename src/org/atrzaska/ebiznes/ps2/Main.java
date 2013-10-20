package org.atrzaska.ebiznes.ps2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.LoglikelihoodSimilarity;

public class Main {
    protected static List<String> tytuly = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("start");

        MovieRecommender movieRecommender = new MovieRecommender();
        // wybór sposobu liczenia podobieństwa
//        movieRecommender.setItemSimilarity(new LogLikelihoodSimilarity(movieRecommender.getModel()));
        movieRecommender.setItemSimilarity(new UncenteredCosineSimilarity(movieRecommender.getModel()));
//        movieRecommender.setItemSimilarity(new EuclideanDistanceSimilarity(movieRecommender.getModel()));

        // szczegoly uzytkownika 5
        movieRecommender.printUserDetails(5);

//      System.out.println(sim.itemSimilarity(1, 8)); // stopien podobienstwa filmu 1 z filmem 8
//      System.out.println(sim.itemSimilarity(1, 6)); // stopien podobienstwa filmu 1 z filmem 6
//      System.out.println(sim.itemSimilarity(1, 5)); // stopien podobienstwa filmu 1 z filmem 5
//      System.out.println(recom.recommend(1, 3)); // wygeneruj 3 rekomendacje na podstawie dodatkowego modelu dla uzytkownika 1
//      System.out.println(recom.mostSimilarItems(1, 2)); // wygeneruj 2 rekomendacje na podstawie oszacowanego podobieństwa dla filmu 1

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("generuje rekomendacje dla uzytkownikow");
        // generuj rekomenacje dla uzytownikow
        for (int i = 0; i < movieRecommender.getModel().getNumUsers(); i++) {
            movieRecommender.printRecommendations(i+1, 3);
        }

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("stop");
    }
}
