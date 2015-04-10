package com.nordnet.topaze.logger.mock;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.domain.Tracage;
import com.nordnet.topaze.logger.repository.TracageRepository;
import com.nordnet.topaze.logger.service.TracageService;
import com.nordnet.topaze.logger.util.Constants;

/**
 * 
 * @author anisselmane.
 * 
 */
public class LogMock implements TracageService {

	/**
	 * le tracage repository. {@link TracageRepository}.
	 */
	@Autowired
	private TracageRepository tracageRepository;

	/**
	 * Constructeur par defaut.
	 */
	public LogMock() {

	}

	@Override
	public void ajouterTrace(String target, String key, String descr, String user) throws TopazeException {
		Tracage trace = new Tracage(target, key, descr, null, user, Constants.TYPE_LOG, new Date());

		tracageRepository.save(trace);
	}
}
