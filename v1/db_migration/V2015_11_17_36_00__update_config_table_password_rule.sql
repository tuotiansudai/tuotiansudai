BEGIN ;
UPDATE
  config
SET
  VALUE = '^(?=.*[^\\d])(.{6,20})$',
  DESCRIPTION = '{0}长度为6-20位，不能全为数字'
WHERE id = 'input.password';

COMMIT ;
