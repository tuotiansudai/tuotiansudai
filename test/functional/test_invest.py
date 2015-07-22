from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage


class TestLogin(BaseSeleniumTestCase):
    def test_should_navigate_to_index_page_after_login(self):
        LoginPage(self.selenium).login('13699248263', '...')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("seekmm", username)

