package org.epnoi.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by cbadenes on 22/12/15.
 */
@ToString
@EqualsAndHashCode(of={"uri"})
public abstract class Relation implements Serializable {

    @Getter
    @Setter
    String uri;

    @Getter
    @Setter
    String creationTime;

    public abstract void setStart(Resource start);

    public abstract void setEnd(Resource end);

    public abstract Resource getStart();

    public abstract Resource getEnd();

    public abstract void setProperties(Relation.Properties properties);

    public enum Type{

        SOURCE_PROVIDES_DOCUMENT(Resource.Type.SOURCE,Resource.Type.DOCUMENT),
        SOURCE_COMPOSES_DOMAIN(Resource.Type.SOURCE,Resource.Type.DOMAIN),
        DOMAIN_CONTAINS_DOCUMENT(Resource.Type.DOMAIN,Resource.Type.DOCUMENT),
        DOCUMENT_BUNDLES_ITEM(Resource.Type.DOCUMENT,Resource.Type.ITEM),
        DOCUMENT_SIMILAR_TO_DOCUMENT(Resource.Type.DOCUMENT,Resource.Type.DOCUMENT),
        DOCUMENT_DEALS_WITH_TOPIC(Resource.Type.DOCUMENT,Resource.Type.TOPIC),
        ITEM_DEALS_WITH_TOPIC(Resource.Type.ITEM,Resource.Type.TOPIC),
        ITEM_SIMILAR_TO_ITEM(Resource.Type.ITEM,Resource.Type.ITEM),
        PART_DESCRIBES_ITEM(Resource.Type.PART,Resource.Type.ITEM),
        PART_SIMILAR_TO_PART(Resource.Type.PART,Resource.Type.PART),
        PART_DEALS_WITH_TOPIC(Resource.Type.PART,Resource.Type.TOPIC),
        TOPIC_EMERGES_IN_DOMAIN(Resource.Type.TOPIC,Resource.Type.DOMAIN),
        TOPIC_MENTIONS_WORD(Resource.Type.TOPIC,Resource.Type.WORD),
        WORD_PAIRS_WITH_WORD(Resource.Type.WORD,Resource.Type.WORD),
        WORD_EMBEDDED_IN_DOMAIN(Resource.Type.WORD,Resource.Type.DOMAIN)
        ;

        private final Resource.Type start;
        private final Resource.Type end;

        public Resource.Type getStart(){
            return start;
        }

        public Resource.Type getEnd(){
            return end;
        }

        Type(Resource.Type start, Resource.Type end){
            this.start  = start;
            this.end    = end;
        }
    }


    public static class Properties{
        Double weight;
        String date;
        String domain;
        String description;
        Long times;

        public void setTimes(Long times){
            this.times = times;
        }

        public Long getTimes(){
            return this.times;
        }

        public void setWeight(Double weight){
            this.weight = weight;
        }

        public Double getWeight(){
            return this.weight;
        }

        public void setDate(String date){
            this.date = date;
        }

        public String getDate(){
            return this.date;
        }

        public void setDomain(String domain){
            this.domain = domain;
        }

        public String getDomain(){
            return this.domain;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public String getDescription(){
            return this.description;
        }

        @Override
        public String toString() {
            return new StringBuilder("[").
                    append("Weight=").append(weight).append("|").
                    append("Date=").append(date).append("|").
                    append("Domain=").append(domain).append("|").toString();
        }
    }

}
