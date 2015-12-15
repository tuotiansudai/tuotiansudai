# -*- coding: utf-8 -*-
from HTMLParser import HTMLParser
from scripts.data_migration import logger

from scripts.data_migration.base import BaseMigrate


class MyHTMLParser(HTMLParser):
    MAPPING = {u'身份证': 1, u'房本证件': 2, u'他项权证': 3, u'借款合同': 4, u'房屋强制执行公证书': 5, u'打款凭条': 6, u'其他': 7}

    def __init__(self):
        HTMLParser.__init__(self)
        self.name_imgs = {}
        self.is_span_tag = False
        self.last_span = None

    def parse(self, data):
        if not data:
            return {}
        self.feed(data)
        result = {}
        for name in self.name_imgs:
            for title in self.MAPPING:
                if title in name:
                    result[self.MAPPING[title]] = self.name_imgs[name]
                    break
        return result

    def handle_starttag(self, tag, attrs):
        if tag == "img":
            attrs_hash = dict(attrs)
            if attrs_hash['src']:
                label = u'其他'
                if self.last_span:
                    label = self.last_span
                imgs = self.name_imgs.get(label, [])
                imgs.append(attrs_hash['src'])
                self.name_imgs[label] = imgs

        if tag == "span":
            self.is_span_tag = True

    def handle_endtag(self, tag):
        if tag == "span":
            self.is_span_tag = False

    def handle_data(self, data):
        if self.is_span_tag and data:
            self.last_span = data


class LoanTitleRelationMigrate(BaseMigrate):

    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT `id` , `guarantee_company_description`  FROM `loan` WHERE `status` NOT IN ('test', 'verify_fail')"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO loan_title_relation VALUES(%s, %s, %s, %s)"

    _index = 0

    def generate_params(self, old_row):
        result = []
        title_id_to_imgs = MyHTMLParser().parse(old_row['guarantee_company_description'])
        # logger.info(title_id_to_imgs)
        for title_id in title_id_to_imgs:
            self._index += 1
            title_id_to_imgs = MyHTMLParser().parse(old_row['guarantee_company_description'])
            values = (self._index, old_row['id'], title_id, ','.join(title_id_to_imgs[title_id]))
            result.append(values)
        return result
