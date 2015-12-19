#-*- coding:utf-8 -*-
from test.functional.pages.base_page import BasePage


class LoanPage(BasePage):
    url = "/loan/313502507433984"

    def click_invest(self):
        self.find_element_by_css(".btn-pay").click();
        return self


