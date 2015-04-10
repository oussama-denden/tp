package com.nordnet.topaze.logger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.logger.domain.Tracage;

/**
 * Outils de persistence pour l'entite {@link Tracage}.
 * 
 * @author anisselmane.
 */
@Repository("tracageRepository")
public interface TracageRepository extends JpaRepository<Tracage, Integer> {

	@Override
	public <S extends Tracage> S save(S arg0);
}
