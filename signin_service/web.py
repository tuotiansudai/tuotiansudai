from flask import Flask
from models import db
from views import sign_in

app = Flask(__name__)
app.register_blueprint(sign_in)
app.config.from_object('settings')
db.init_app(app)


if __name__ == "__main__":
    app.run()
