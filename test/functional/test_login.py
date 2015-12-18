from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.login_page import LoginPage

class TestLogin(BaseSeleniumTestCase):
    def test_should_navigate_to_index_page_after_login(self):
        doc = LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = doc.find_element_by_css("div.header a[href='/personal-info']").text
        self.assertIn("shenjiaojiao", username)

