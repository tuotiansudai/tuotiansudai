import datetime
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import func
from sqlalchemy.ext.declarative import declared_attr

db = SQLAlchemy()


class User(db.Model):
    __tablename__ = 'user'

    username = db.Column(db.String(80), unique=True, name='login_name', primary_key=True)
    mobile = db.Column(db.String(11), unique=True)
    password = db.Column(db.String(40))
    salt = db.Column(db.String(40))

    def __repr__(self):
        return '<User %r>' % self.username


class UserRole(db.Model):
    __tablename__ = 'user_role'

    user = db.relationship('User', backref=db.backref('roles', lazy='joined'))
    username = db.Column(db.String(80), db.ForeignKey('user.login_name'), primary_key=True, name='login_name')
    role = db.Column(db.String(20), primary_key=True)

    def __repr__(self):
        return '<Role %s:%s>' % (self.role, self.username)


class LoginLog(db.Model):
    @declared_attr
    def __tablename__(cls):
        return "login_log_%s" % datetime.datetime.now().strftime('%Y%m')

    id = db.Column(db.BIGINT, primary_key=True)
    login_name = db.Column(db.String(25))
    source = db.Column(db.String(10))
    ip = db.Column(db.String(16))
    device = db.Column(db.String(255))
    login_time = db.Column(db.DateTime(timezone=True), default=func.now())
    success = db.Column(db.Boolean)
