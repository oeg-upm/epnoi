package org.epnoi.uia.core;

import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.ParametersModelReader;

import java.util.logging.Logger;

public class CoreUtility {
    // ---------------------------------------------------------------------------------
    private static final Logger logger = Logger.getLogger(CoreUtility.class
            .getName());

    public static Core getUIACore() {

        return getUIACore(null);

    }

    public static Core getUIACore(String configurationFile) {

        long time = System.currentTimeMillis();

        //logger.info("Reading the following paramaters for the UIA: " + parametersModel);

        Core core = CoreSpringUtility.getCore(configurationFile);

        long afterTime = System.currentTimeMillis();
        logger.info("It took " + (Long) (afterTime - time) / 1000.0
                + "to load the UIA core");

        return core;

    }

    public static ParametersModel readParameters(String configurationFile) {
        ParametersModel parametersModel = null;

        try {
            parametersModel = ParametersModelReader.read(configurationFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parametersModel;
    }

}