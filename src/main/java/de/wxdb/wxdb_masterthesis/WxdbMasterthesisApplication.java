package de.wxdb.wxdb_masterthesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Führt das System aus.
 *
 * @author Kaan Mustafa Celik
 */
@SpringBootApplication
@EnableFeignClients
public class WxdbMasterthesisApplication {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(WxdbMasterthesisApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WxdbMasterthesisApplication.class, args);
		log.info("✅ WXDB Backend-System erfolgreich gestartet.");
	}

}
