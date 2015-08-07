#coding=utf-8

from test.functional.pages.base_page import BasePage

class LoanDXB(BasePage):
    url = "/p2p_loan-list_dxb"

    #选择一个定向标
    def get_one_DXB(self):
        self.find_element_by_xpath("//a[@href='/p2p_loan_dxb/20150729000151']").click()
        return self