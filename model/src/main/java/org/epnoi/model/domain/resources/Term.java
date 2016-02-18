package org.epnoi.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Term extends Resource {

	//private String[] words;
	public static final String CONTENT="content";
	private String content = "";

	public static final String LENGTH="length";
	private int length;

	public static final String OCURRENCES="ocurrences";
	private long ocurrences;

	public static final String SUBTERMS="subterms";
	private long subterms;

	public static final String SUPERTERMS="superterms";
	private long superterms;

	// TODO this value should be moved to APPEARED_IN relation
	public static final String CVALUE="cvalue";
	private double cvalue;

	// TODO this value should be moved to APPEARED_IN relation
	public static final String CONSENSUS="consensus";
	private double consensus;

	public static final String PERTINENCE="pertinence";
	private double pertinence;

	// TODO this value should be moved to APPEARED_IN relation
	public static final String PROBABILITY="pertinence";
	private double probability;

	// TODO this value should be moved to APPEARED_IN relation
	public static final String TERMHOOD="termhood";
	private double termhood;


//	public static String buildURI(String term, String domain) {
//		String uri = "http://" + domain + "/"
//				+ StringUtils.replace(term, "[^a-zA-Z0-9]", "_");
//		return uri;
//
//	}

}
