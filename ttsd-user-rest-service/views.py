# coding=utf-8
from flask import request, Blueprint
from flask.json import jsonify
from flask.views import MethodView

import service
from forms import LoginForm, RefreshTokenForm, LoginAfterRegisterForm, UserRegisterForm, UserUpdateForm, \
    UserResetPasswordForm, UserChangePasswordForm, UserQueryForm, QueryLimitForm

sign_in = Blueprint('sign_in', __name__)


def success(data={}, code=200):
    ret = {'result': True}
    ret.update(data)
    return jsonify(ret), code


def fail(data={}, code=401):
    ret = {'result': False}
    ret.update(data)
    return jsonify(ret), code


@sign_in.route("/login/", methods=['POST'])
def login():
    form = LoginForm(request.form)
    if form.validate():
        manager = service.LoginManager(form, request.headers.get('x-forwarded-for'))
        result = manager.login()
        return success(result) if result['result'] else fail(result)
    return fail({'message': form.errors}, code=400)


@sign_in.route("/login/nopassword/", methods=['POST'])
def login_without_password():
    form = LoginAfterRegisterForm(request.form)
    if form.validate():
        manager = service.LoginManager(form, request.headers.get('x-forwarded-for'))
        result = manager.no_password_login()
        return success(result) if result['result'] else fail(result)
    return fail({'message': form.errors}, code=400)


@sign_in.route("/logout/<session_id>", methods=['POST'])
def logout(session_id):
    manager = service.SessionManager()
    user_info = manager.delete(session_id)
    token_id = manager.create()
    return success({'token': token_id, 'user_info': user_info})


@sign_in.route("/session/<session_id>", methods=['GET'])
def get_session(session_id):
    source = request.args.get('source', 'WEB')
    user_info = service.SessionManager(source=source).get(session_id)
    if user_info:
        return success({'user_info': user_info, 'token': session_id})
    return fail()


@sign_in.route("/session/<session_id>", methods=['PUT'])
def refresh_session_data(session_id):
    source = request.args.get('source', 'WEB')
    user_info = service.refresh_session_data(session_id, source)
    if user_info:
        return success({'user_info': user_info, 'token': session_id})
    return fail()


@sign_in.route("/refresh/<session_id>", methods=['POST'])
def refresh_session(session_id):
    form = RefreshTokenForm(request.form)
    if form.validate():
        new_session_id = service.SessionManager(source=form.source.data).refresh(session_id)
        return get_session(new_session_id)
    return fail({'message': form.errors}, code=400)


@sign_in.route("/user/<username>/active/", methods=['POST'])
def active_user(username):
    service.active(username)
    return success()


class UsersView(MethodView):
    def post(self):
        """create user"""
        form = UserRegisterForm(data=request.get_json())
        if form.validate():
            user_service = service.UserService()
            try:
                u = user_service.create(form)
                return success({'user_info': u}, code=201)
            except Exception as ex:
                return fail({'message': ex.message}, code=400)
        else:
            return fail({'errors': form.errors}, code=400)

    def get(self):
        """search user"""
        form = UserQueryForm(request.args)
        if form.validate():
            user_service = service.UserService()
            return success(user_service.query(form))
        else:
            return fail({'errors': form.errors}, code=400)


class UserView(MethodView):
    def get(self, login_name_or_mobile):
        """find by login_name or mobile"""
        user_service = service.UserService()
        u = user_service.find_by_login_name_or_mobile(login_name_or_mobile)
        if u:
            return success({'user_info': u})
        else:
            return fail({'user_info': None}, code=404)

    def put(self):
        """update or patch"""
        form = UserUpdateForm(data=request.get_json())
        if form.validate():
            user_service = service.UserService()
            try:
                u = user_service.update(form)
                return success({'user_info': u}, code=200)
            except Exception as ex:
                return fail({'message': ex.message}, code=400)
        else:
            return fail({'errors': form.errors}, code=400)


sign_in.add_url_rule('/users', view_func=UsersView.as_view('users'))
sign_in.add_url_rule('/user', view_func=UserView.as_view('update_user'), methods=['PUT', ])
sign_in.add_url_rule('/user/<string:login_name_or_mobile>', view_func=UserView.as_view('get_user'), methods=['GET', ])


@sign_in.route("/users/province-empty", methods=['GET'])
def search_user_with_province_empty():
    form = QueryLimitForm(request.args)
    if form.validate():
        user_service = service.UserService()
        try:
            result = user_service.empty_province_users(limit=form.limit.data)
            return success(result, code=200)
        except Exception as ex:
            return fail({'message': ex.message}, code=400)
    else:
        return fail({'errors': form.errors}, code=400)


@sign_in.route("/user/reset-password", methods=['PUT'])
def user_reset_password():
    form = UserResetPasswordForm(data=request.get_json())
    if form.validate():
        user_service = service.UserService()
        try:
            u = user_service.reset_password(form)
            return success({'user_info': u}, code=200)
        except service.UserNotExistedError:
            return fail({'message': u'用户不存在'}, code=400)
        except Exception as ex:
            return fail({'message': ex.message}, code=400)
    else:
        return fail({'errors': form.errors}, code=400)


@sign_in.route("/user/change-password", methods=['POST'])
def user_change_password():
    form = UserChangePasswordForm(data=request.get_json())
    if form.validate():
        user_service = service.UserService()
        try:
            u = user_service.change_password(form)
            return success({'user_info': u}, code=200)
        except service.UserNotExistedError:
            return fail({'message': u'用户不存在'}, code=400)
        except service.UsernamePasswordError:
            return fail({'message': u'原密码错误'}, code=401)
        except Exception as ex:
            return fail({'message': ex.message}, code=400)
    else:
        return fail({'errors': form.errors}, code=400)
