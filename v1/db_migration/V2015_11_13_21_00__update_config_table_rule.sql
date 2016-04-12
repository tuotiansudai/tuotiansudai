BEGIN ;
UPDATE
  config
SET
  VALUE = '^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,20})$',
  DESCRIPTION = '{0}是数字和字母的组合，长度为6-20位'
WHERE id = 'input.password';


UPDATE
  config
SET
  VALUE = '(?!^\\d+$)^\\w{5,25}$',
  DESCRIPTION = '{0}格式不正确，长度为5-25位，请勿使用手机号'
WHERE id = 'input.username';
COMMIT ;