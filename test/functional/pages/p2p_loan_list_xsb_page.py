#coding=utf-8
from test.functional.pages.base_page import BasePage

class LoanXSB(BasePage):
    url = "/p2p_loan-list_xsb"

    #选择一个新手标
    def get_one_XSB(self):
        self.find_element_by_xpath("//a[@href='/p2p_loan_xsb/20150722000142']").click()
        return self