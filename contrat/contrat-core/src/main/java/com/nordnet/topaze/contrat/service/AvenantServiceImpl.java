package com.nordnet.topaze.contrat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.TypeAvenant;
import com.nordnet.topaze.contrat.repository.AvenantRepository;

/**
 * implementation des services de la classe {@link AvenantService}.
 * 
 * @author akram-moncer
 * 
 */
@Service("avenantService")
public class AvenantServiceImpl implements AvenantService {

	/**
	 * {@link AvenantRepository}.
	 */
	@Autowired
	private AvenantRepository avenantRepository;

	@Override
	public Avenant findByReferenceContratAndVersionAndTypeAvenant(String referenceContrat, Integer version,
			TypeAvenant typeAvenant) {
		return avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(referenceContrat, version, typeAvenant);
	}

	@Override
	public Avenant findByReferenceContratAndVersion(String referenceContrat, Integer version) {
		return avenantRepository.findByReferenceContratAndVersion(referenceContrat, version);
	}

	@Override
	public List<Avenant> findAvenantAvecCessionActive() {
		return avenantRepository.findAvenantAvecCessionActive();
	}

	@Override
	public List<Avenant> findAvenantAvecMigrationActive() {
		return avenantRepository.findAvenantAvecMigrationActive();
	}

	@Override
	public Avenant findAvenantAvecCessionActive(String referenceContrat) {
		return avenantRepository.findAvenantAvecCessionActive(referenceContrat);
	}

	@Override
	public Avenant findAvenantAvecMigrationActive(String referenceContrat) {
		return avenantRepository.findAvenantAvecMigrationActive(referenceContrat);
	}

	@Override
	public List<Avenant> findAvenantAvecRenouvellementActive() {
		return avenantRepository.findAvenantAvecRenouvellementActive();
	}

	@Override
	public Avenant findAvenantAvecRenouvellementActive(String referenceContrat) {
		return avenantRepository.findAvenantAvecRenouvellementActive(referenceContrat);
	}

	@Override
	public List<Integer> findReferenceAvenantAvecCessionActive() {
		return avenantRepository.findReferenceAvenantAvecCessionActive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> findReferenceAvenantAvecMigrationActive() {
		return avenantRepository.findReferenceAvenantAvecMigrationActive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Avenant findByID(Integer id) {
		return avenantRepository.findOne(id);
	}
}
