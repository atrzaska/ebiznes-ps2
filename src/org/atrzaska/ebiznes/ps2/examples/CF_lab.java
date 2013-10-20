package org.atrzaska.ebiznes.ps2.examples;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class CF_lab {
    public static void main(String[] args) throws TasteException, IOException {
        // utworzenie modelu danych z pliku
        DataModel model = new FileDataModel(new File("data/gr2.csv"));
        LongPrimitiveIterator i = model.getUserIDs();// wypisanie id użytkowników

        while(i.hasNext()) {
            Long u = (Long)i.next();
            System.out.print(u + ", ");
        }

        System.out.println();
        System.out.println(model.getMaxPreference());
        System.out.println(model.getMinPreference());
        System.out.println(model.getItemIDsFromUser(1)); // wypisanie przedmiotów zakupionych przez użytkownika
        System.out.println(model.getNumItems());

        // wybór sposobu liczenia podobieństwa
        ItemSimilarity sim = new LogLikelihoodSimilarity(model);
        //ItemSimilarity sim = new UncenteredCosineSimilarity(model);
        //ItemSimilarity sim = new EuclideanDistanceSimilarity(model);

        //tworzenie systemu rekomendującego i generowanie rekomendacji
        GenericItemBasedRecommender recom = new GenericItemBasedRecommender(model, sim);

        System.out.println(sim.itemSimilarity(1, 8)); // wypisanie wybranych podobieństw
        System.out.println(sim.itemSimilarity(1, 6));
        System.out.println(sim.itemSimilarity(1, 5));
        System.out.println(recom.recommend(1, 2)); // rekomendacje na podstawiedodatkowego modelu
        System.out.println(recom.mostSimilarItems(1, 2)); // rekomendacje na podstawie oszacowanego podobieństwa
    }
}
