BEGIN;

UPDATE loan la,
(SELECT concat(15,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2015' AND product_type != 'EXPERIENCE' AND pledge_type= 'HOUSE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2015';

UPDATE loan la,
(SELECT concat(15,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2015' AND product_type != 'EXPERIENCE' AND pledge_type= 'VEHICLE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2015';

UPDATE loan la,
(SELECT concat(16,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2016' AND product_type != 'EXPERIENCE' AND pledge_type= 'HOUSE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2016';

UPDATE loan la,
(SELECT concat(16,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2016' AND product_type != 'EXPERIENCE' AND pledge_type= 'HOUSE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2016';

UPDATE loan la,
(SELECT concat(17,LPAD((@rowNO := @rowNo+1),3,'0')) AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2017' AND product_type != 'EXPERIENCE' AND pledge_type= 'VEHICLE' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2017';

COMMIT;
