INSERT INTO `url_mapping` VALUES ('news', '/news/#{termId}', 'themepath:term.htm', '新闻公告');

INSERT INTO `category_term` VALUES ('announcement', 'ARTICLE', '网站公告', NULL, '', '5', '');
INSERT INTO `category_term` VALUES ('mediareport', 'ARTICLE', '媒体报道', NULL, '媒体报道', '0', '');

update category_term_node set term_id ='announcement' where term_id ='wangzhangonggao' ;
update category_term_node set term_id ='mediareport' where term_id ='meitibaodao' ;

DELETE from category_term where id in ('wangzhangonggao','meitibaodao');


