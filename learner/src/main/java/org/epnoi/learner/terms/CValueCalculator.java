package org.epnoi.learner.terms;

import org.epnoi.model.domain.resources.Term;

public class CValueCalculator {
	private static final double LENGHT_ONE_WEIGTH = 0.5;

	public static double calculateCValue(Term termCandidate) {

		double lenghtWeight;
		if (termCandidate.getLength() == 1) {
			lenghtWeight = LENGHT_ONE_WEIGTH;
		} else {
			lenghtWeight = termCandidate.getLength()
					* (Math.log(termCandidate.getLength()) / Math
							.log(2));
		}

		double cValue;

		if (termCandidate.getSuperterms() == 0) {
			cValue = lenghtWeight
					* termCandidate.getOcurrences();

		} else {
			cValue = lenghtWeight
					* (termCandidate.getOcurrences() - (termCandidate
							.getSubterms() / termCandidate
							.getSuperterms()));

		}

		return cValue;
	}
}
