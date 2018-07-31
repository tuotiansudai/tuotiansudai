import hashlib
import uuid
from datetime import datetime
from random import choice
from string import ascii_lowercase

from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


class User(db.Model):
    __tablename__ = 'user'

    login_name = db.Column(db.String(25), unique=True, primary_key=True)
    mobile = db.Column(db.String(11), unique=True)
    password = db.Column(db.String(40))
    salt = db.Column(db.String(32))
    last_login_time = db.Column(db.DateTime(timezone=True))
    last_login_source = db.Column(db.String(16))

    email = db.Column(db.String(100))
    user_name = db.Column(db.String(50))
    identity_number = db.Column(db.String(18))
    ump_user_name = db.Column(db.String(50))
    ump_identity_number = db.Column(db.String(18))
    register_time = db.Column(db.DateTime(timezone=True), nullable=False)
    last_modified_time = db.Column(db.DateTime(timezone=True))
    last_modified_user = db.Column(db.String(25))
    referrer = db.Column(db.String(25))
    status = db.Column(db.String(20), nullable=False)
    channel = db.Column(db.String(32))
    province = db.Column(db.String(32))
    city = db.Column(db.String(32))
    source = db.Column(db.String(16))

    def __init__(self, mobile, referrer, channel, source):
        self.login_name = self._generate_login_name()
        self.mobile = mobile
        self.referrer = referrer
        self.channel = channel
        self.source = source
        self.register_time = datetime.now()
        self.status = 'ACTIVE'
        self.last_modified_time = datetime.now()

    def set_password(self, raw_password):
        self.salt = uuid.uuid4().hex
        self.password = self._encode_password(raw_password)

    def validate_password(self, raw_password):
        return self.password == self._encode_password(raw_password)

    def _encode_password(self, raw_password):
        return hashlib.sha1(u"%s{%s}" % (hashlib.sha1(raw_password.encode('utf-8')).hexdigest(), self.salt)).hexdigest()

    def _generate_login_name(self):

        def random_string(length):
            return ''.join(choice(ascii_lowercase) for i in range(length))

        while True:
            login_name = random_string(8)
            if not User.query.filter((User.login_name == login_name)).first():
                return login_name

    def as_dict(self):
        def __fmt(value):
            if isinstance(value, datetime):
                return value.strftime('%Y-%m-%d %H:%M:%S')
            return value

        return {c.name: __fmt(getattr(self, c.name)) for c in self.__table__.columns if c.name not in ('salt', 'password')}

    @staticmethod
    def lookup_field(field_name):
        return User.__dict__[field_name]

    def __repr__(self):
        return '<User %r>' % self.login_name


class UserRole(db.Model):
    __tablename__ = 'user_role'

    user = db.relationship('User', backref=db.backref('roles', lazy='joined'))
    login_name = db.Column(db.String(80), db.ForeignKey('user.login_name'), primary_key=True)
    role = db.Column(db.String(20), primary_key=True)
    created_time = db.Column(db.DateTime(timezone=True))

    def __init__(self, login_name, role):
        self.login_name = login_name
        self.role = role
        self.created_time = datetime.now()

    def __repr__(self):
        return '<Role %s:%s>' % (self.role, self.login_name)
