BEGIN;

INSERT `membership_price` (`level`, `duration`, `price`) VALUES (5, 30, 2500);
INSERT `membership_price` (`level`, `duration`, `price`) VALUES (5, 180, 12000);
INSERT `membership_price` (`level`, `duration`, `price`) VALUES (5, 360, 18000);

COMMIT;
