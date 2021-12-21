package project.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(
            LogService.class);

    public void info(String input) {
        logger.info(input);
    }

    public void debug(String input) {
        logger.debug(input);
    }

    public void error(String input) {
        logger.error(input);
    }
}
