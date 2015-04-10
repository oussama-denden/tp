package com.nordnet.topaze.livraison.core.domain;


/**
 * Enumeration que definit le status de {@link BonPreparation}.
 * <p>
 * trois types Status:
 * </p>
 * <ul>
 * <li><b>INITIER</b> : la peremiere phase juste apres la creation du bon.</li>
 * <li><b>NON_LIVRER</b> : si les produit est non livre/non active</li>
 * <li><b>TERMINER</b> : soit livre, soit retourne</li>
 * <li><b>PREPARER</b> : une fois que le poduit est active (si service) ou livre (si bien).</li>
 * <li><b>EN_COURS_PREPARATION</b> : le bon est en cours de preparation.</li>
 * <li><b>ANNULER</b> : une fois que le bp est annule.</li>
 * </ul>
 * 
 * @author akram-moncer
 * 
 */
public enum StatusBonPreparation {
	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #INITIER}.
	 */
	INITIER,
	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #TERMINER}.
	 */
	TERMINER,
	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #NON_LIVRER}.
	 */
	NON_LIVRER,

	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #ANNULER}.
	 */
	ANNULER,

	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #EN_COURS_PREPARATION}.
	 */
	EN_COURS_PREPARATION,

	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #PREPARER}.
	 */
	PREPARER,

	/**
	 * Indique que le {@link StatusBonPreparation} de {@link BonPreparation} est {@link #EN_ATTENTE}.
	 */
	EN_ATTENTE,

}