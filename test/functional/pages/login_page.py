import test.functional.session as session
from test.functional.pages.base_page import BasePage


class LoginPage(BasePage):
    url = "/login"

    def username(self, username):
        self.find_element_by_name("username").send_keys(username)
        return self

    def password(self, password):
        self.find_element_by_name("password").send_keys(password)
        return self

    def captcha(self):
        session_id = self.get_session_id()
        captcha = session.get_login_captcha(session_id)
        self.find_element_by_name("captcha").send_keys(captcha)
        return self

    def click_login(self):
        self.find_element_by_css(".login-submit").click()
        return self

    def login(self, username, password):
        self.username(username).password(password).captcha().click_login()
        return self


