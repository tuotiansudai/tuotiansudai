FROM tiangolo/uwsgi-nginx-flask:python2.7

MAINTAINER Shi Lei <mleoshi@gmail.com>

COPY requirements.txt /app/

RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r /app/requirements.txt

WORKDIR /app