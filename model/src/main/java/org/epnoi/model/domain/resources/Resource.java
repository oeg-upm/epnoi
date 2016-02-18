package org.epnoi.model.domain.resources;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
		TERM("term","terms",Term.class),
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

		public static Type from(Class clazz){
			List<Type> types = Arrays.stream(values()).filter(x -> x.getClass().equals(clazz)).collect(Collectors.toList());

			if (types == null || types.size() > 1) throw new IllegalArgumentException("No type found for class: " + clazz);

			return types.get(0);

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
