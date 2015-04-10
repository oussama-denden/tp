=======
CHANGELOG from 2015-01-09
===================

This changelog references the relevant changes (bug fixes and minor features) done
from 2015-01-09.

 * topaze-finder
  * 400 bad request exception handling in topaze-finder controler
  * Improving topaze-finder business exception handling
  * Adding the field "label" to the response of REST web service GET /contrat/{refContrat} 
  * Handling TopazeException in topaze-finder controler
  * Adding REST web service GET /contrat/espaceclient/client/{idClient} 
  * Rounding all TTC values to two decimals places
  * Fixing bug #422: "dateRenouvellement" and "dateFin" can never be both null
 
 * contrat core
  * Adding Payment Mode VIREMENT
  * Resigning multiple contracts POST /contrat/resilier
  * Changing PolitiqueResiliation.commentaireAnnulaion field's name to commentaireAnnulation
  * If "engagement" == null then "dateFinEngagement" will be set to null
  * For the "Frais" Reduction the "TypeValeur" can't be "MOIS"
  * US 411 add reference to reduction entity and improve the cancelling process.
  * bug 418 fix the change timestamp field to be showed like a date in trame reduction.
 
 * businessprocess-facture
  * When payement mode is Virement we set mouvement's payment mode to other
  * Fixing Bug #411
  * Fixing Bug #412
  * Fixing Bug #417: Setting BillingGroup for the first recurrent movement
  * Updating the mouvement's productLabel 
  * Implementing user story #374: Reviewing the implementation of the ProductId 
  * Adding dependency ot-netcatalog
  * Updating config file: env.properties 
  * Fixing a bug about product label for selling contract
 
 * topaze-client
  * US 143 : Add payment mode 'VIREMENT'.

 * resiliation-outil
  * Fixing Bug #405

 * businessprocess-packager
  * If the state of the packager instance is activable or active then mark the topaze service element as delivered 

 * swagger
  * Fix missing Enum values with swagger.
  
 * Billing-outil
  * add json ignore to fix remboursement issue.
  