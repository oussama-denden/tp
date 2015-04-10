package com.nordnet.topaze.contrat.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modp.checkdigits.CheckDigit;
import com.modp.checkdigits.LRICRCISO7064Mod97_10;
import com.nordnet.topaze.contrat.domain.Keygen;
import com.nordnet.topaze.contrat.repository.KeygenRepository;
import com.nordnet.topaze.contrat.util.Constants;

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
		keygenRepository.save(keygen);

		// ADD CRC
		CheckDigit crc = new LRICRCISO7064Mod97_10();
		newReference = crc.encode(newReference);

		LOGGER.info("Fin methode getNextKey Class = " + clazz.getName());
		return newReference;
	}
}
