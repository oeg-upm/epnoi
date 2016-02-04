package org.epnoi.model.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@ToString
@EqualsAndHashCode(of={"uri"})
public class Resource implements Serializable{

	public static final String URI="uri";
	String uri;

	public static final String CREATION_TIME="creationTime";
	String creationTime;

	public String getUri(){
		return uri;
	}

	public void setUri(String uri){
		this.uri = uri;
	}

	public String getCreationTime(){
		return creationTime;
	}

	public void setCreationTime(String creationTime){
		this.creationTime = creationTime;
	}


	public enum Type{
		SOURCE("source", Source.class),
		DOMAIN("domain",Domain.class),
		DOCUMENT("document",Document.class),
		ITEM("item", Item.class),
		PART("part", Part.class),
		WORD("word", Word.class),
		ANALYSIS("analysis", Analysis.class),
		TOPIC("topic", Topic.class),
		ANY("*", Resource.class);

		String keyValue;

		Class classOf;

		Type(String key, Class classOf){
			this.keyValue = key;
			this.classOf = classOf;
		}

		public String key(){ return keyValue;}

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
