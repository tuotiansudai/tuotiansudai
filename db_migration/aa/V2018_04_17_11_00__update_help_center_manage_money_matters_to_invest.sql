BEGIN;
UPDATE help_center set title = replace(title, '理财', '投资'), content = replace(content, '理财', '投资');
COMMIT;


