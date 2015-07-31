#coding=utf-8
from test.functional.pages.base_page import BasePage

class LoanJXB(BasePage):
    url = "/p2p_loan-list_jxb"

    #选择一个加息标
    def get_one_JXB(self):
        self.find_element_by_xpath("//a[@href='/p2p_loan_jxb/20150422000054']").click()
        return self
