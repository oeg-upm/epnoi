package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.epnoi.model.domain.LinkableElement;
import org.epnoi.model.domain.Resource;

/**
 * Created by cbadenes on 22/12/15.
 */
@ToString
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class Relation extends LinkableElement {

    @Getter @Setter
    protected String startUri;

    @Getter @Setter
    protected String endUri;

    public abstract Resource.Type getStartType();

    public abstract Resource.Type getEndType();

    public abstract Double getWeight();

    //TODO This Type should be only defined in each Relation (constructor)
    public enum Type{

        SOURCE_PROVIDES_DOCUMENT(Resource.Type.SOURCE,Resource.Type.DOCUMENT,"provides"),
        SOURCE_COMPOSES_DOMAIN(Resource.Type.SOURCE,Resource.Type.DOMAIN,"composes"),
        DOMAIN_CONTAINS_DOCUMENT(Resource.Type.DOMAIN,Resource.Type.DOCUMENT,"contains"),
        DOCUMENT_BUNDLES_ITEM(Resource.Type.DOCUMENT,Resource.Type.ITEM,"bundles"),
        DOCUMENT_SIMILAR_TO_DOCUMENT(Resource.Type.DOCUMENT,Resource.Type.DOCUMENT,"similarTo"),
        DOCUMENT_DEALS_WITH_TOPIC(Resource.Type.DOCUMENT,Resource.Type.TOPIC,"dealsWith"),
        ITEM_DEALS_WITH_TOPIC(Resource.Type.ITEM,Resource.Type.TOPIC,"dealsWith"),
        ITEM_SIMILAR_TO_ITEM(Resource.Type.ITEM,Resource.Type.ITEM,"similarTo"),
        PART_DESCRIBES_ITEM(Resource.Type.PART,Resource.Type.ITEM,"describes"),
        PART_SIMILAR_TO_PART(Resource.Type.PART,Resource.Type.PART,"similarTo"),
        PART_DEALS_WITH_TOPIC(Resource.Type.PART,Resource.Type.TOPIC,"dealsWith"),
        TOPIC_EMERGES_IN_DOMAIN(Resource.Type.TOPIC,Resource.Type.DOMAIN,"emergesIn"),
        TOPIC_MENTIONS_WORD(Resource.Type.TOPIC,Resource.Type.WORD,"mentions"),
        WORD_PAIRS_WITH_WORD(Resource.Type.WORD,Resource.Type.WORD,"pairsWith"),
        WORD_EMBEDDED_IN_DOMAIN(Resource.Type.WORD,Resource.Type.DOMAIN,"embeddedIn"),
        TERM_MENTIONS_WORD(Resource.Type.TERM,Resource.Type.WORD,"mentions"),
        TERM_APPEARED_IN_DOMAIN(Resource.Type.TERM,Resource.Type.DOMAIN,"appearedIn"),
        TERM_HYPERNYM_OF_TERM(Resource.Type.TERM, Resource.Type.TERM,"hypernymOf")
        ;

        private final Resource.Type start;
        private final Resource.Type end;
        private final String key;

        public Resource.Type getStart(){
            return start;
        }

        public Resource.Type getEnd(){
            return end;
        }

        public String getKey() { return key; }

        Type(Resource.Type start, Resource.Type end,String key){
            this.start  = start;
            this.end    = end;
            this.key    = key;
        }
    }

}
