package com.nordnet.topaze.finder.business.welcome;

import java.util.ArrayList;
import java.util.List;

/**
 * Bilan des reduction d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
public class ReductionBilan {

	/**
	 * Reduction global.
	 */
	private Reduction reductionContrat;

	/**
	 * Les reduction applique sur les elementcontractuels.
	 */
	private List<Reduction> reductionsEC = new ArrayList<>();

	/**
	 * Constructeur par defaut.
	 */
	public ReductionBilan() {

	}

	/**
	 * @return {@link #reductionContrat}.
	 */
	public Reduction getReductionContrat() {
		return reductionContrat;
	}

	/**
	 * @param reductionContrat
	 *            {@link #reductionContrat}.
	 */
	public void setReductionContrat(Reduction reductionContrat) {
		this.reductionContrat = reductionContrat;
	}

	/**
	 * @return {@link #reductionsEC}.
	 */
	public List<Reduction> getReductionsEC() {
		return reductionsEC;
	}

	/**
	 * @param reductionsEC
	 *            {@link #reductionsEC}.
	 */
	public void setReductionsEC(List<Reduction> reductionsEC) {
		this.reductionsEC = reductionsEC;
	}
}