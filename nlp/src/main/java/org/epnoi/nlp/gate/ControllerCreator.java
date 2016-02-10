package org.epnoi.nlp.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import org.epnoi.model.parameterization.ParametersModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;

@Component
public class ControllerCreator {

	private static final Logger LOG = LoggerFactory.getLogger(ControllerCreator.class);

	@Value("${epnoi.nlp.gatePath}")
	String gatePath;


	public SerialAnalyserController createController() {
		// In this piece of code we just initialize the processing resources.
		// Gate + the associated plugins are initialized in the core
		// initialization
		try {

			LOG.info("Creating new controller from GATE: " + gatePath + " ..");

		//	System.out.println("......> " + this.core);

			String grammarsPath = gatePath + "/grammars/nounphrases";

			SerialAnalyserController controller = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");

			ProcessingResource reseter = (ProcessingResource) Factory
					.createResource("gate.creole.annotdelete.AnnotationDeletePR");

			ProcessingResource englishTokeniser = (ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser");

			ProcessingResource sentenceSplitter = (ProcessingResource) Factory
					.createResource("gate.creole.splitter.RegexSentenceSplitter");

			ProcessingResource POStagger = (ProcessingResource) Factory
					.createResource("gate.creole.POSTagger");
			FeatureMap dependencyParserFeature = Factory.newFeatureMap();
			dependencyParserFeature.put("addConstituentAnnotations", false);
			dependencyParserFeature.put("reusePosTags", true);
			dependencyParserFeature.put("addDependencyFeatures", false);

			/*
			 * ProcessingResource dependencyParser = (ProcessingResource)
			 * Factory .createResource("gate.stanford.Parser",
			 * dependencyParserFeature);
			 */
			FeatureMap mainGrammarFeature = Factory.newFeatureMap();
			mainGrammarFeature.put("grammarURL", new File(grammarsPath
					+ "/main.jape").toURI().toURL());

			LanguageAnalyser mainGrammarTransducer = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer",
							mainGrammarFeature);
			controller.add(reseter);
			controller.add(englishTokeniser);
			controller.add(sentenceSplitter);
			controller.add(POStagger);
			// sac.add(dependencyParser);
			controller.add(mainGrammarTransducer);

			return controller;
		} catch (MalformedURLException | ResourceInstantiationException e) {
			LOG.error("Error creating a new controller", e);
		}
		return null;
	}
}
