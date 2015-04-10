package com.nordnet.topaze.businessprocess.packager.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.businessprocess.packager.domain.Keygen;
import com.nordnet.topaze.businessprocess.packager.repository.KeygenRepository;
import com.nordnet.topaze.businessprocess.packager.util.Constants;

/**
 * L'implementation de service {@link KeygenService}.
 * 
 * @author anisselmane.
 * 
 */
@Service("keygenService")
public class KeygenServiceImpl implements KeygenService {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(KeygenServiceImpl.class);

	/**
	 * {@link KeygenRepository}.
	 */
	@Autowired
	private KeygenRepository keygenRepository;

	/**
	 * {@inheritDoc}.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public String getNextKey(Class clazz) {
		LOGGER.info("Enter methode getNextKey  Class = " + clazz.getName());

		Keygen keygen = keygenRepository.findDernier(clazz.getName());

		long inc = 0L;
		if (keygen != null) {
			inc = Long.parseLong(keygen.getReference()) + 1;
		} else {
			keygen = new Keygen();
			keygen.setEntite(clazz.getName());
			inc = Long.parseLong(Constants.REF_INIT);
		}

		// generer la nouvelle reference draft.

		String newReference = String.format("%08d", inc);
		keygen.setReference(newReference);
		keygen.setEntite(clazz.getName());
		keygenRepository.save(keygen);

		LOGGER.info("Fin methode getNextKey Class = " + clazz.getName());
		return newReference;
	}
}
