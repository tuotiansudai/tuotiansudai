FROM tiangolo/uwsgi-nginx-flask

MAINTAINER Shi Lei <mleoshi@gmail.com>

COPY requirements.txt /app/

RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r /app/requirements.txt

COPY ./mns_python_sdk /app/mns_python_sdk
RUN cd /app/mns_python_sdk && pip install .

WORKDIR /app