#coding=utf-8
from test.functional.pages.base_page import BasePage

class LoanlistPage(BasePage):
    url = "/p2p_loan-list"

    #选择一个普通标
    def get_one_PTB(self):
        self.find_element_by_xpath("//a[@href='/p2p_loan/20150427000062']").click()
        return self
