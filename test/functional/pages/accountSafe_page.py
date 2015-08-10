#coding=utf-8
from test.functional.pages.base_page import BasePage

class accountSafePage(BasePage):
    url="/user/accountSafe"

    def changePassword(self):
        self.find_element_by_xpath("//a[@href='/user/changePassword']").click()
        return self

    def change_binding_email(self):
        self.find_element_by_xpath("//a[@href='/user/binding_email']").click()
        return self

    def change_binding_mobile(self):
        self.find_element_by_xpath("//a[@href='/user/change_binding_mobile']").click()