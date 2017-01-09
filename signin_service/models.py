import time
from flask_sqlalchemy import SQLAlchemy

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
