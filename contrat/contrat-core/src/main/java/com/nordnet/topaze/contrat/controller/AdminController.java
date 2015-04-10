package com.nordnet.topaze.contrat.controller;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

@Api(value = "admin", description = "admin")
@Controller
@RequestMapping("/admin")
public class AdminController {

		public static class Versioning {
			private final String ver;
			private final String date;
			
			public Versioning(final String ver, final String date) {
				this.ver = ver;
				this.date = date;
			}
			
			public String getVersion() {
				return ver;
			}
			
			public String getDate() {
				return date;
			}
		}
	
		/**
		 * Declaration du log.
		 */
		private final static Logger LOGGER = Logger.getLogger(AdminController.class);

		@RequestMapping(value = "/version", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Versioning getVersion() throws TopazeException {
			LOGGER.info(":::ws-rec:::getVersion");
			
			Resource resource = new ClassPathResource("/version.properties");
			
			try {
				final Properties props = PropertiesLoaderUtils.loadProperties(resource);
				return new Versioning(props.getProperty("topaze.version"), props.getProperty("topaze.build.date"));
			} catch(IOException ioe) {
				throw new TopazeException("Unreadable version.properties file, fix the file.", ioe);
			}
		}

}
