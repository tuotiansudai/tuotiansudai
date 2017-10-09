from flask import request, Blueprint
from flask.json import jsonify
from flask.views import MethodView

import service
from forms import LoginForm, RefreshTokenForm, LoginAfterRegisterForm

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


@sign_in.route("/refresh/<session_id>", methods=['POST'])
def refresh_session(session_id):
    form = RefreshTokenForm(request.form)
    if form.validate():
        new_session_id = service.SessionManager(source=form.source.data).refresh(session_id)
        return get_session(new_session_id)
    return fail({'message': form.errors}, code=400)


@sign_in.route("/user/<username>/active/", methods=['POST'])
def active_user(username):
    print 'GET /user active'
    service.active(username)
    return success()


class UsersView(MethodView):
    def post(self):
        """create user"""
        user_service = service.UserService()
        try:
            u = user_service.create(request.get_json())
            return success({'user_info': u}, code=201)
        except Exception as ex:
            return fail({'message': ex.message}, code=400)

    def get(self):
        """search user"""
        # TODO
        # fields = request.args.get('fields')
        # page_size = request.args.get('page_size', default=10, type=int)
        # page_index = request.args.get('page_index', default=0, type=int)

        print 'GET /users'
        return success({})


class UserView(MethodView):
    def get(self):
        """find by login_name or mobile"""
        query_login_name = request.args.get("login_name")
        query_mobile = request.args.get("mobile")
        query_login_name_or_mobile = request.args.get("login_name_or_mobile")
        user_service = service.UserService()
        u = None
        if query_login_name:
            u = user_service.find_by_login_name(query_login_name)
        elif query_mobile:
            u = user_service.find_by_mobile(query_mobile)
        elif query_login_name_or_mobile:
            u = user_service.find_by_login_name_or_mobile(query_login_name_or_mobile)
        return success({'user_info': u})

    def put(self):
        """update or patch"""
        user_service = service.UserService()
        try:
            u = user_service.update(request.get_json())
            return success({'user_info': u}, code=200)
        except Exception as ex:
            return fail({'message': ex.message}, code=400)


sign_in.add_url_rule('/users', view_func=UsersView.as_view('users'))
sign_in.add_url_rule('/user', view_func=UserView.as_view('user'))
