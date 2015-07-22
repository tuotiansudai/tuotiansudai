from test.functional.pages.base_page import BasePage


class LoginPage(BasePage):
    url = "/memberLoginPage"

    def username(self, username):
        self.find_element_by_id("username").send_keys(username)
        return self

    def password(self, password):
        self.find_element_by_id("password").send_keys(password)
        return self

    def click_login(self):
        self.find_element_by_id("butt").click()
        return self

    def login(self, username, password):
        self.username(username).password(password).click_login()


