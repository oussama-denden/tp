package com.nordnet.topaze.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.catalogue.domain.Produit;

/**
 * Outils de persistence pour l'entite {@link Produit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Repository("produitRepository")
public interface ProduitRepository extends JpaRepository<Produit, Integer> {

	/**
	 * Cherecher le produit avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference produit
	 * @return produit avec le reference passée en parametre.
	 */
	public Produit findByReference(String reference);

}
