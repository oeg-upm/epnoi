package org.epnoi.model.domain.resources;

import com.sun.java.browser.plugin2.DOM;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.LinkableElement;

@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class Resource extends LinkableElement {

	public abstract Type getResourceType();

	public enum Type{
		SOURCE("source","sources"),
		DOMAIN("domain","domains"),
		DOCUMENT("document","documents"),
		ITEM("item","items"),
		PART("part","parts"),
		WORD("word","words"),
		ANALYSIS("analysis","analyses"),
		TOPIC("topic","topics"),
		SERIALIZED_OBJECT("object","objects"),
		TERM("term","terms"),
		ANY("*","*");

		String plural;
		String key;

		Type(String key, String plural){
			this.key = key;
			this.plural = plural;
		}

		public String key(){ return key;}

		public String route(){ return plural;}
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

	public static Class classOf(Type type){
		switch(type){
			case ANALYSIS:return Analysis.class;
			case DOCUMENT: return Document.class;
			case DOMAIN: return Domain.class;
			case ITEM: return Item.class;
			case PART: return Part.class;
			case SERIALIZED_OBJECT: return SerializedObject.class;
			case SOURCE: return Source.class;
			case TERM: return Term.class;
			case TOPIC: return Topic.class;
			case WORD: return Word.class;
			default: return Resource.class;
		}
	}

	public static Analysis newAnalysis(){
		return new Analysis();
	}

	public static Document newDocument(){
		return new Document();
	}

	public static Domain newDomain(){
		return new Domain();
	}

	public static Item newItem(){
		return new Item();
	}

	public static Part newPart(){
		return new Part();
	}

	public static SerializedObject newSerializedObject(){
		return new SerializedObject();
	}

	public static Source newSource(){
		return new Source();
	}

	public static Term newTerm(){
		return new Term();
	}

	public static Topic newTopic(){
		return new Topic();
	}

	public static Word newWord(){
		return new Word();
	}

	public Analysis asAnalysis(){
		return Analysis.class.cast(this);
	}

	public Document asDocument(){
		return Document.class.cast(this);
	}

	public Domain asDomain(){
		return Domain.class.cast(this);
	}

	public Item asItem(){
		return Item.class.cast(this);
	}

	public Part asPart(){
		return Part.class.cast(this);
	}

	public SerializedObject asSerializedObject(){
		return SerializedObject.class.cast(this);
	}

	public Source asSource(){
		return Source.class.cast(this);
	}

	public Term asTerm(){
		return Term.class.cast(this);
	}

	public Topic asTopic(){
		return Topic.class.cast(this);
	}

	public Word asWord(){
		return Word.class.cast(this);
	}

}
