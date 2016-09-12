BEGIN;
USE aa;
-- move invest point
INSERT INTO point_bill_temp SELECT *
                            FROM point_bill
                            WHERE business_type = 'INVEST';
-- move lottery point
INSERT INTO point_bill_temp SELECT *
                            FROM point_bill
                            WHERE business_type = 'LOTTERY';
COMMIT;
