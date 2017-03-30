BEGIN;

UPDATE loan la,
(SELECT RIGHT(DATE_FORMAT(created_time,'%Y'),2) AS lastYearTwo FROM loan WHERE DATE_FORMAT(created_time,'%Y') = '2015') lb,
(SELECT LPAD((@rowNO := @rowNo+1),3,'0') AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2015' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lb.lastYearTwo, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2015';

UPDATE loan la,
(SELECT RIGHT(DATE_FORMAT(created_time,'%Y'),2) AS lastYearTwo FROM loan WHERE DATE_FORMAT(created_time,'%Y') = '2016') lb,
(SELECT LPAD((@rowNO := @rowNo+1),3,'0') AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2016' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lb.lastYearTwo, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2016';

UPDATE loan la,
(SELECT RIGHT(DATE_FORMAT(created_time,'%Y'),2) AS lastYearTwo FROM loan WHERE DATE_FORMAT(created_time,'%Y') = '2017') lb,
(SELECT LPAD((@rowNO := @rowNo+1),3,'0') AS loanNameSeq, id AS id FROM loan AS a, (select @rowNO :=0) b WHERE DATE_FORMAT(created_time,'%Y') = '2017' ORDER BY created_time ASC) lc
SET la.name = concat(la.name, lb.lastYearTwo, lc.loanNameSeq)
WHERE la.id = lc.id
AND DATE_FORMAT(la.created_time,'%Y') = '2017';

COMMIT;
