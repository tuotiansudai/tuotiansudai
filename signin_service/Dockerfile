FROM tiangolo/uwsgi-nginx-flask

MAINTAINER Shi Lei <mleoshi@gmail.com>

COPY requirements.txt /app/

RUN pip install -r /app/requirements.txt

WORKDIR /app

CMD ["/usr/bin/supervisord"]