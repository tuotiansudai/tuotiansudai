from django.shortcuts import redirect
from django.urls import reverse


def user_roles_check(roles=()):
    def user_roles_check_decorator(fun):
        def wrapper(request, *args, **kwargs):
            if [role for role in roles if role in request.session['roles']]:
                return fun(request, *args, **kwargs)
            else:
                return redirect(reverse("index"))

        return wrapper

    return user_roles_check_decorator
