package org.epnoi.knowledgebase.wikidata;

/**
 * Created by cbadenes on 10/02/16.
 */
public enum DumpProcessingMode {
    JSON, CURRENT_REVS, ALL_REVS, CURRENT_REVS_WITH_DAILIES, ALL_REVS_WITH_DAILIES, JUST_ONE_DAILY_FOR_TEST;

    public static DumpProcessingMode from(String value){
        return DumpProcessingMode.valueOf(value.toUpperCase());
    }
}
