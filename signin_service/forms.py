from werkzeug.routing import ValidationError
import wtforms


class LoginForm(wtforms.Form):
    username = wtforms.StringField('username',
                                   [wtforms.validators.required(), wtforms.validators.Length(min=5, max=25)])
    password = wtforms.StringField('password',
                                   [wtforms.validators.required(), wtforms.validators.Length(min=6, max=20)])

    token = wtforms.StringField('token', [wtforms.validators.required()])
    source = wtforms.StringField('source')
    device_id = wtforms.StringField('device_id')


class LoginAfterRegisterForm(wtforms.Form):
    username = wtforms.StringField('username',
                                   [wtforms.validators.required(), wtforms.validators.Length(min=5, max=25)])
    token = wtforms.StringField('token', [wtforms.validators.required()])
    source = wtforms.StringField('source')
    device_id = wtforms.StringField('device_id')


class SourceForm(wtforms.Form):
    source = wtforms.StringField('source', [wtforms.validators.required()])


class RefreshTokenForm(SourceForm):

    def validate_source(self, field):
        if field.data not in ('IOS', 'ANDROID'):
            raise ValidationError('Name must be less than 50 characters')
