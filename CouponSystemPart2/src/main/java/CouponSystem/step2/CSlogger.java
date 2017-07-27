package CouponSystem.step2;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CSlogger {

	private Logger logger;
	private FileHandler fileHandler;
	private SimpleFormatter formatter;
	private static CSlogger instance;

	private CSlogger() {
		try {
			logger = Logger.getLogger(CSlogger.class.getName());
			fileHandler = new FileHandler("logFile.log", true);
			formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.addHandler(fileHandler);
			logger.setLevel(Level.INFO);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized CSlogger getInstance() {
		if (instance == null) {
			instance = new CSlogger();
		}
		return instance;

	}

	public Logger getLogger() {
		return logger;
	}

}
