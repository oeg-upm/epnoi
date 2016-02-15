package org.epnoi.model.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"})
public class Resource extends LinkableElement {


	public enum Type{
		SOURCE("source","sources", Source.class),
		DOMAIN("domain","domains",Domain.class),
		DOCUMENT("document","documents",Document.class),
		ITEM("item","items", Item.class),
		PART("part","parts", Part.class),
		WORD("word","words", Word.class),
		ANALYSIS("analysis","analyses", Analysis.class),
		TOPIC("topic","topics", Topic.class),
		SERIALIZED_OBJECT("object","objects",SerializedObject.class),
		ANY("*","*", Resource.class);

		String plural;
		String key;

		Class classOf;

		Type(String key, String plural, Class classOf){
			this.key = key;
			this.classOf = classOf;
			this.plural = plural;
		}

		public String key(){ return key;}

		public String plural(){ return plural;}

		public Class classOf(){
			return this.classOf;
		}

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
