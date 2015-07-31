#coding=utf-8
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.p2p_loan_list_xsb_page import LoanXSB
from test.functional.pages.p2p_loan_xsb_20150722000142_page import InvestXSB


class TestinvestXS(BaseSeleniumTestCase):
    def test_login_investXS(self):
        LoginPage(self.selenium).login('xuyuanting11', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("xuyuanting11", username)

        investXS_page = IndexPage(self.selenium).get_lend().get_investXS()
        self.assertIn("/p2p_loan-list_xsb", investXS_page.get_current_page_url())

        p2ploanlist_page = LoanXSB(self.selenium).get_one_XSB()
        self.assertIn("/p2p_loan_xsb/20150722000142", p2ploanlist_page.get_current_page_url())

        xsb_page = InvestXSB(self.selenium).input_invest_money(1)
        xsb_page.click_invest_button().accept()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)