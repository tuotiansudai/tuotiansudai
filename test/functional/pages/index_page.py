#-*- coding:utf-8 -*-
from test.functional.pages.base_page import BasePage


class IndexPage(BasePage):
    url = "/"

    def get_username(self):
        return self.find_element_by_css(".personal-info-link").text

    def click_prepare_invest(self):
        self.find_element_by_link_text("立即投资").click();
        return self

    def can_loan_detail(self):
        try:
            self.find_element_by_link_text("立即投资")
            return True
        except:
            return False


