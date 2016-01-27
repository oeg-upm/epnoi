package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString
public class Resource implements Serializable{

    private String uri;

    private String creationTime;

    public enum Type{
        SOURCE("source"),
        DOMAIN("domain"),
        DOCUMENT("document"),
        ITEM("item"),
        PART("part"),
        WORD("word"),
        RELATION("relation"),
        ANALYSIS("analysis"),
        TOPIC("topic"),
        ANY("*");

        String keyValue;

        Type(String key){ keyValue = key;}

        public String key(){ return keyValue;}

    }

    public enum State {
        CREATED("created"),
        UPDATED("updated"),
        DELETED("deleted"),
        ANY("*");

        String keyValue;

        State(String key){ keyValue = key;}

        public String key(){ return keyValue;}
    }

}
