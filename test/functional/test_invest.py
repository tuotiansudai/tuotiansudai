import time
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.loan_page import LoanPage


class TestInvest(BaseSeleniumTestCase):
    def test_should_navigate_to_loan_page_after_login(self):
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        index_page = IndexPage(self.selenium)
        can_loan_detail = index_page.can_loan_detail()

        self.assertEqual(True,can_loan_detail)

        curr_url = index_page.click_prepare_invest().get_current_page_url()
        loan_page =  LoanPage(self.selenium,curr_url)
        can_invest = loan_page.can_invest()

        self.assertEqual(True,can_invest)

        ump_url = loan_page.click_invest().get_current_page_url()

        self.assertEqual("http://pay.soopay.net/spay/pay/payservice.do",ump_url)













