from django.conf import settings
from django.contrib.auth.decorators import login_required
from django.shortcuts import redirect
from django.urls import reverse


def console_login_required(fun=None):
    if fun:
        def login_check(request, *args, **kwargs):
            if request.session['token']:
                return fun(request, *args, **kwargs)

        return login_required(login_check, login_url=settings.LOGIN_URL)


def user_roles_check(roles=()):
    def user_roles_check_decorator(fun):
        def wrapper(request, *args, **kwargs):
            if [role for role in roles if request.session['roles'].filter(name=role)]:
                return fun(request, *args, **kwargs)
            else:
                return redirect(reverse("index"))

        return login_required(wrapper, login_url=settings.LOGIN_URL)

    return user_roles_check_decorator
