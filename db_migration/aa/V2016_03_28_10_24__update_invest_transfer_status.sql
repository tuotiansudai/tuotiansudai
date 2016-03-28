BEGIN ;
update invest set transfer_status = 'NONTRANSFERABLE' WHERE loan_id in (select id from loan where status <>'REPAYING');
END;