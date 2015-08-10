#coding=utf-8
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.center_page import CentPage
from test.functional.pages.accountSafe_page import accountSafePage
from test.functional.pages.changePassword_page import changePassword

import time

class TestChangePasswd(BaseSeleniumTestCase):

    def test_login_after_change_password(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)
        #我的账户
        IndexPage(self.selenium).get_my_account()
        #账户管理->安全信息
        myaccount_page = CentPage(self.selenium).get_the_private_message()
        self.assertIn("/user/accountSafe", myaccount_page.get_current_page_url())

        #修改密码页面
        changePassword_page = accountSafePage(self.selenium).changePassword()
        self.assertIn("/user/changePassword", changePassword_page.get_current_page_url())

        #修改密码
        update_password = changePassword(self.selenium).get_the_old_passwd('123abc')
        update_password.get_the_new_passwd('123aaa').get_the_repasswd('123aaa').changePasswordButton()

        check_message = update_password.get_alert_message()
        self.assertIn(u"密码修改成功！", check_message)
    def test_login_after_change_repassword(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123aaa')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)
        #我的账户
        IndexPage(self.selenium).get_my_account()
        #账户管理->安全信息
        myaccount_page = CentPage(self.selenium).get_the_private_message()
        self.assertIn("/user/accountSafe", myaccount_page.get_current_page_url())

        #修改密码页面
        changePassword_page = accountSafePage(self.selenium).changePassword()
        self.assertIn("/user/changePassword", changePassword_page.get_current_page_url())

        #修改密码
        update_password = changePassword(self.selenium).get_the_old_passwd('123aaa')
        update_password.get_the_new_passwd('123abc').get_the_repasswd('123abc').changePasswordButton()

        check_message = update_password.get_alert_message()
        self.assertIn(u"密码修改成功！", check_message)