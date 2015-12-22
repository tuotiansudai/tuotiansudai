from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.recharge_page import RechargePage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.index_page import IndexPage

class TestRecharge(BaseSeleniumTestCase):
    def test_recharge(self):
        LoginPage(self.selenium).login("shenjiaojiao","123abc")
        login_username = IndexPage(self.selenium).get_username()
        self.assertEqual("shenjiaojiao", login_username)

        recharge = RechargePage(self.selenium)

        if recharge.find_element_by_css(".fast-recharge-tab"):
            aaa
        else:
            recharge.find_element_by_css(".e-bank-recharge-tab")
