BEGIN;
UPDATE answer set answer = replace(answer, '理财', '投资');
UPDATE question set question = replace(question, '理财', '投资'), addition = replace(addition, '理财', '投资');
COMMIT;


