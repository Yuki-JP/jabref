package org.jabref.logic.importer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import org.jabref.logic.importer.fetcher.ACMPortalFetcher;
import org.jabref.logic.importer.fetcher.ACS;
import org.jabref.logic.importer.fetcher.ArXiv;
import org.jabref.logic.importer.fetcher.AstrophysicsDataSystem;
import org.jabref.logic.importer.fetcher.CiteSeer;
import org.jabref.logic.importer.fetcher.CrossRef;
import org.jabref.logic.importer.fetcher.DBLPFetcher;
import org.jabref.logic.importer.fetcher.DOAJFetcher;
import org.jabref.logic.importer.fetcher.DiVA;
import org.jabref.logic.importer.fetcher.DoiFetcher;
import org.jabref.logic.importer.fetcher.DoiResolution;
import org.jabref.logic.importer.fetcher.GoogleScholar;
import org.jabref.logic.importer.fetcher.GvkFetcher;
import org.jabref.logic.importer.fetcher.IEEE;
import org.jabref.logic.importer.fetcher.INSPIREFetcher;
import org.jabref.logic.importer.fetcher.IacrEprintFetcher;
import org.jabref.logic.importer.fetcher.IsbnFetcher;
import org.jabref.logic.importer.fetcher.LibraryOfCongress;
import org.jabref.logic.importer.fetcher.MathSciNet;
import org.jabref.logic.importer.fetcher.MedlineFetcher;
import org.jabref.logic.importer.fetcher.OpenAccessDoi;
import org.jabref.logic.importer.fetcher.RfcFetcher;
import org.jabref.logic.importer.fetcher.ScienceDirect;
import org.jabref.logic.importer.fetcher.SpringerFetcher;
import org.jabref.logic.importer.fetcher.SpringerLink;
import org.jabref.logic.importer.fetcher.TitleFetcher;
import org.jabref.logic.importer.fetcher.ZbMATH;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.identifier.DOI;
import org.jabref.model.entry.identifier.Identifier;

import static org.jabref.model.entry.field.StandardField.EPRINT;
import static org.jabref.model.entry.field.StandardField.ISBN;

public class WebFetchers {

    private WebFetchers() {
    }

    public static Optional<IdBasedFetcher> getIdBasedFetcherForField(Field field, ImportFormatPreferences preferences) {
        IdBasedFetcher fetcher;

        if (field == StandardField.DOI) {
            fetcher = new DoiFetcher(preferences);
        } else if (field == ISBN) {
            fetcher = new IsbnFetcher(preferences);
        } else if (field == EPRINT) {
            fetcher = new ArXiv(preferences);
        } else {
            return Optional.empty();
        }
        return Optional.of(fetcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Identifier> IdFetcher<T> getIdFetcherForIdentifier(Class<T> clazz) {
        if (clazz == DOI.class) {
            return (IdFetcher<T>) new CrossRef();
        } else {
            throw new IllegalArgumentException("No fetcher found for identifier" + clazz.getCanonicalName());
        }
    }

    public static Optional<IdFetcher<? extends Identifier>> getIdFetcherForField(Field field) {
        if (field == StandardField.DOI) {
            return Optional.of(new CrossRef());
        }
        return Optional.empty();
    }

    /**
     * @return sorted list containing search based fetchers
     */
    public static SortedList<SearchBasedFetcher> getSearchBasedFetchers(ImportFormatPreferences importFormatPreferences) {
        ArrayList<SearchBasedFetcher> list = new ArrayList<>();
        list.add(new ArXiv(importFormatPreferences));
        list.add(new INSPIREFetcher(importFormatPreferences));
        list.add(new GvkFetcher());
        list.add(new MedlineFetcher());
        list.add(new AstrophysicsDataSystem(importFormatPreferences));
        list.add(new MathSciNet(importFormatPreferences));
        list.add(new ZbMATH(importFormatPreferences));
        list.add(new ACMPortalFetcher(importFormatPreferences));
        list.add(new GoogleScholar(importFormatPreferences));
        list.add(new DBLPFetcher(importFormatPreferences));
        list.add(new SpringerFetcher());
        list.add(new CrossRef());
        list.add(new CiteSeer());
        list.add(new DOAJFetcher(importFormatPreferences));
        list.add(new IEEE(importFormatPreferences));

        ObservableList<SearchBasedFetcher> observableList = FXCollections.observableList(list);
        return new SortedList<>(observableList, Comparator.comparing(WebFetcher::getName));
    }

    /**
     * @return sorted list containing id based fetchers
     */
    public static SortedList<IdBasedFetcher> getIdBasedFetchers(ImportFormatPreferences importFormatPreferences) {
        ArrayList<IdBasedFetcher> list = new ArrayList<>();
        list.add(new ArXiv(importFormatPreferences));
        list.add(new AstrophysicsDataSystem(importFormatPreferences));
        list.add(new IsbnFetcher(importFormatPreferences));
        list.add(new DiVA(importFormatPreferences));
        list.add(new DoiFetcher(importFormatPreferences));
        list.add(new MedlineFetcher());
        list.add(new TitleFetcher(importFormatPreferences));
        list.add(new MathSciNet(importFormatPreferences));
        list.add(new CrossRef());
        list.add(new LibraryOfCongress(importFormatPreferences));
        list.add(new IacrEprintFetcher(importFormatPreferences));
        list.add(new RfcFetcher(importFormatPreferences));

        ObservableList<IdBasedFetcher> observableList = FXCollections.observableList(list);
        return new SortedList<>(observableList, Comparator.comparing(WebFetcher::getName));
    }

    /**
     * @return sorted list containing entry based fetchers
     */
    public static SortedList<EntryBasedFetcher> getEntryBasedFetchers(ImportFormatPreferences importFormatPreferences) {
        ArrayList<EntryBasedFetcher> list = new ArrayList<>();
        list.add(new AstrophysicsDataSystem(importFormatPreferences));
        list.add(new DoiFetcher(importFormatPreferences));
        list.add(new IsbnFetcher(importFormatPreferences));
        list.add(new MathSciNet(importFormatPreferences));
        list.add(new CrossRef());

        ObservableList<EntryBasedFetcher> observableList = FXCollections.observableList(list);
        return new SortedList<>(observableList, Comparator.comparing(WebFetcher::getName));
    }

    /**
     * @return sorted list containing id fetchers
     */
    public static SortedList<IdFetcher> getIdFetchers(ImportFormatPreferences importFormatPreferences) {
        ArrayList<IdFetcher> list = new ArrayList<>();
        list.add(new CrossRef());
        list.add(new ArXiv(importFormatPreferences));

        ObservableList<IdFetcher> observableList = FXCollections.observableList(list);
        return new SortedList<>(observableList, Comparator.comparing(WebFetcher::getName));
    }

    /**
     * @return unsorted list containing fulltext fetchers
     */
    public static List<FulltextFetcher> getFullTextFetchers(ImportFormatPreferences importFormatPreferences) {
        List<FulltextFetcher> fetchers = new ArrayList<>();
        // Original
        fetchers.add(new DoiResolution());
        // Publishers
        fetchers.add(new ScienceDirect());
        fetchers.add(new SpringerLink());
        fetchers.add(new ACS());
        fetchers.add(new ArXiv(importFormatPreferences));
        fetchers.add(new IEEE(importFormatPreferences));
        // Meta search
        fetchers.add(new GoogleScholar(importFormatPreferences));
        fetchers.add(new OpenAccessDoi());

        return fetchers;
    }
}
