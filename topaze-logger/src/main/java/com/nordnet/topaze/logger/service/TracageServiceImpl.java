package com.nordnet.topaze.logger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.rest.RestClient;
import com.nordnet.topaze.logger.util.Constants;

/**
 * L'implementation de service {@link TracageService}.
 * 
 * @author anisselmane.
 * 
 */
@Service("tracageService")
public class TracageServiceImpl implements TracageService {

	/**
	 * client REST.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void ajouterTrace(String target, String key, String descr, String user) throws TopazeException {

		restClient.addLog(target, key, descr, null, user, Constants.TYPE_LOG);
	}

}
