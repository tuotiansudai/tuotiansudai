DELIMITER $$

DROP FUNCTION IF EXISTS `removeHtml`$$

CREATE DEFINER = `root` @`%` FUNCTION `removeHtml` (description_html TEXT) RETURNS TEXT CHARSET utf8
BEGIN
  WHILE
    LOCATE('<', description_html) != 0 DO SET description_html = REPLACE(
      description_html,
      SUBSTRING(
        description_html,
        LOCATE('<', description_html),
        LOCATE('>', description_html) - LOCATE('<', description_html) + 1
      ),
      ''
    ) ;
  END WHILE ;
  RETURN (description_html) ;
END $$

DELIMITER ;
