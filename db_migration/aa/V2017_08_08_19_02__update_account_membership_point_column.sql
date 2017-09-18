BEGIN;

UPDATE account,
(
SELECT * FROM(
SELECT SUM(meb.experience) AS sum_eb, a.membership_point AS mp, a.login_name
FROM account a JOIN membership_experience_bill meb
ON a.login_name = meb.login_name
GROUP BY a.login_name) b
WHERE sum_eb != mp) temp
SET account.membership_point = temp.sum_eb
WHERE account.login_name = temp.login_name;


COMMIT;