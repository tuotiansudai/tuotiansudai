#!/bin/bash

clear_cache(){
    find . -type f -name "*.py[co]" -delete
    find . -type d -name "__pycache__" -delete
}

install(){
    echo "install..."
    pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt
    echo "install done"
}

ut(){
    cp /workspace/deploy-config/ttsd-current/settings_local.py .
    python manage.py test --noinput
}

clear_cache && install && ut