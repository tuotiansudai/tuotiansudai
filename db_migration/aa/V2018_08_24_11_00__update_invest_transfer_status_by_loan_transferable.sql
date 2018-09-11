
UPDATE invest set transfer_status = 'NONTRANSFERABLE' where loan_id in (SELECT loan_id from loan_details where non_transferable = 1)
