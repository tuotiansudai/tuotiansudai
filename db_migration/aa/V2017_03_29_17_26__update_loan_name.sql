BEGIN;

UPDATE loan la,
(SELECT concat(15,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2015' AND product_type != 'EXPERIENCE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2015';

UPDATE loan la,
(SELECT concat(16,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2016' AND product_type != 'EXPERIENCE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2016';

UPDATE loan la,
(SELECT concat(17,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2017' AND product_type != 'EXPERIENCE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2017';


UPDATE edxmessage.user_message um,
(SELECT l.id, l.name FROM aa.loan AS l, edxmessage.user_message AS eum WHERE l.id = eum.business_id AND l.pledge_type='HOUSE') l
SET um.content = REPLACE(um.content, '房产抵押借款', l.name),
um.title = REPLACE(um.title, '房产抵押借款', l.name)
WHERE um.business_id = l.id
AND um.message_id IN (
SELECT id FROM message WHERE event_type IN ('INVEST_SUCCESS','LOAN_OUT_SUCCESS','REPAY_SUCCESS','ADVANCED_REPAY','TRANSFER_SUCCESS')
);

UPDATE edxmessage.user_message um,
(SELECT l.id, l.name FROM aa.loan AS l, edxmessage.user_message AS eum WHERE l.id = eum.business_id AND l.pledge_type='VEHICLE') l
SET um.content = REPLACE(um.content, '车辆抵押借款', l.name),
um.title = REPLACE(um.title, '车辆抵押借款', l.name)
WHERE um.business_id = l.id
AND um.message_id IN (
SELECT id FROM message WHERE event_type IN ('INVEST_SUCCESS','LOAN_OUT_SUCCESS','REPAY_SUCCESS','ADVANCED_REPAY','TRANSFER_SUCCESS')
);

COMMIT;
