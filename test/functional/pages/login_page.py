#coding=utf-8
from test.functional.pages.base_page import BasePage

class LoginPage(BasePage):
    url = "/memberLoginPage"

    #用户名
    def username(self, username):
        self.find_element_by_id("username").send_keys(username)
        return self

    #密码
    def password(self, password):
        self.find_element_by_id("password").send_keys(password)
        return self

    #登录
    def click_login(self):
        self.find_element_by_id("butt").click()
        return self

    def login(self, username, password):
        self.username(username).password(password).click_login()


