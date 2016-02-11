package org.epnoi.nlp.gate;

import gate.Gate;
import gate.util.GateException;
import org.epnoi.model.parameterization.ParametersModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class GATEInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(GATEInitializer.class);

	@Value("${epnoi.nlp.gatePath}")
	String gatePath;

	
	/**
	 * Initializtion of the Gate natural language processing framework and the
	 * needed Gate plugins
	 */
	@PostConstruct
	public void init() {
		LOG.info("Initializing Gate");
		String pluginsPath = gatePath + "/plugins";
		// String grammarsPath = gateHomePath + "/grammars/nounphrases";

		LOG.info("The gateHomePath is set to " + gatePath
				+ ", the pluginsPath is set to " + pluginsPath);

		File gateHomeDirectory = new File(gatePath);
		File pluginsDirectory = new File(pluginsPath);

		Gate.setPluginsHome(pluginsDirectory);

		Gate.setGateHome(gateHomeDirectory);
		Gate.setUserConfigFile(new File(gateHomeDirectory, "user-gate.xml"));

		try {
			Gate.init(); // to prepare the GATE library

			_initGATEPlugins(pluginsDirectory);

		} catch (MalformedURLException | GateException e) {
			LOG.error("Error initializing GATE",e);
		}

	}

	private void _initGATEPlugins(File pluginsDirectory)
			throws MalformedURLException, GateException {

		//ANNIE Plugin----------------------------------------------------------------------------------
		URL anniePlugin = new File(pluginsDirectory, "ANNIE").toURI().toURL();

		Gate.getCreoleRegister().registerDirectories(anniePlugin);

		/*Desactivated
		URL stanfordCoreNLPPlugin = new File(pluginsDirectory,
				"Parser_Stanford").toURI().toURL();
		Gate.getCreoleRegister().registerDirectories(stanfordCoreNLPPlugin);
		*/
	}
}
