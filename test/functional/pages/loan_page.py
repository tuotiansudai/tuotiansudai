#-*- coding:utf-8 -*-
from test.functional.pages.base_page import BasePage


class LoanPage(BasePage):
    url = "/loan"

    def click_invest(self):
        self.find_element_by_link_text("立即投资").click();
        return self


