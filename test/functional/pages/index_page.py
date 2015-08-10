#coding=utf-8
from test.functional.pages.base_page import BasePage


class IndexPage(BasePage):
    url = "/"

    def get_username(self):
        return self.find_element_by_css(".fr.top-con-right > a[href='/user/center']").text

    #我的账户
    def get_my_account(self):
        self.find_element_by_xpath("//a[@href='/user/center']").click()
        return self

    #我要出借
    def get_lend(self):
         self.find_element_by_link_text("我要出借").click()
         return self

    #新手投资
    def get_investXS(self):
        self.find_element_by_link_text("新手投资").click()
        return self

    #定向投资
    def get_investDX(self):
        self.find_element_by_link_text("定向投资").click()
        return self

    #加息投资
    def get_investJX(self):
        self.find_element_by_link_text("加息投资").click()
        return self