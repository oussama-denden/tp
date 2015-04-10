package com.nordnet.topaze.uploader.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec les promotions.
 * 
 * @author akram-moncer
 * 
 */
@Api(value = "uploader", description = "uploader")
@Controller
public class UploadController {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(UploadController.class);

	/**
	 * 
	 * @param file
	 *            file to upload.
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("templatePath")
							+ file.getOriginalFilename())))) {
				byte[] bytes = file.getBytes();
				stream.write(bytes);
				stream.close();
				return "You successfully uploaded " + file.getOriginalFilename() + " into "
						+ file.getOriginalFilename();
			} catch (Exception e) {
				LOGGER.error("You failed to upload file", e);
				return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
			}
		}
		return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
	}

	/**
	 * Gerer le cas ou on a une exception.
	 * 
	 * @param req
	 *            requete HttpServletRequest.
	 * @param ex
	 *            exception
	 * @return {@link InfoErreur}
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({ TopazeException.class })
	@ResponseBody
	InfoErreur handleTopazeException(HttpServletRequest req, Exception ex) {
		return new InfoErreur(req.getRequestURI(), ((TopazeException) ex).getErrorCode(), ex.getLocalizedMessage());
	}
}
