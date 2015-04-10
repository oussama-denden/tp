=======
CHANGELOG from 2015-02-23
===================

This changelog references the relevant changes (bug fixes and minor features) done
from 2015-02-23.
* topaze
  * change log request to POST.
  * Mock log and alert.
  
* topaze-finder
  * Implementing US #431: Updating the contract's end date based on it's renewall date.
  * change all the cron in topaze to avoid no synchronization between object in the memory and those in the database.
  
 * topaze-exception
  * Adding a new generic error code 0.6: JSON flow parsing error. 
  * fix bug in getting date with time in cancelling process.
  
* contrat-core
  * Add reimbursement from a date.
  * Fix bug cancel renew.
  
* resiliation-outil
  * Add reimbursement from a date.
  
* client-rest
 * Add reimbursement from a date.
   
* businessprocess-facture
  * Add reimbursement from a date.
