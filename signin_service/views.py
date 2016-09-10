from flask import request, Blueprint
from flask.json import jsonify
from forms import LoginForm, RefreshTokenForm, LoginAfterRegisterForm
import service


sign_in = Blueprint('sign_in', __name__)


def success(data={}):
    ret = {'result': True}
    ret.update(data)
    return jsonify(ret), 200


def fail(data={}):
    ret = {'result': False}
    ret.update(data)
    return jsonify(ret), 400


@sign_in.route("/login/", methods=['POST'])
def login():
    form = LoginForm(request.form)
    if form.validate():
        manager = service.LoginManager(form, request.headers.get('x-forwarded-for'))
        result = manager.login()
        return success(result) if result['result'] else fail(result)
    return fail({'message': form.errors})


@sign_in.route("/login/nopassword/", methods=['POST'])
def login_without_password():
    form = LoginAfterRegisterForm(request.form)
    if form.validate():
        manager = service.LoginManager(form, request.headers.get('x-forwarded-for'))
        result = manager.no_password_login()
        return success(result) if result['result'] else fail(result)
    return fail({'message': form.errors})


@sign_in.route("/logout/<session_id>", methods=['POST'])
def logout(session_id):
    manager = service.SessionManager()
    manager.delete(session_id)
    token_id = manager.create()
    return success({'token': token_id})


@sign_in.route("/session/<session_id>", methods=['GET'])
def get_session(session_id):
    user_info = service.SessionManager().get(session_id)
    if user_info:
        return success({'user_info': user_info, 'token': session_id})
    return fail()


@sign_in.route("/refresh/<session_id>", methods=['POST'])
def refresh_session(session_id):
    form = RefreshTokenForm(request.form)
    if form.validate():
        new_session_id = service.SessionManager().refresh(session_id)
        return get_session(new_session_id)
    return fail({'message': form.errors})


@sign_in.route("/user/<username>/active/", methods=['POST'])
def active_user(username):
    service.active(username)
    return success()


