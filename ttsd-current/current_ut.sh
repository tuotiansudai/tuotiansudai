#!/bin/bash

clear_cache(){
    find . -type f -name "*.py[co]" -delete
    find . -type d -name "__pycache__" -delete
}

install(){
    echo "install..."
    source /root/.current/bin/activate
    pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt
    echo "install done"
}

exit_code=0

ut(){
    python manage.py test --noinput --settings current_rest.settings current_rest
    exit_code1=$?
    echo "rest test result: $exit_code1"

    python manage.py test --noinput --settings current_console.settings current_console
    exit_code2=$?
    echo "console test result: $exit_code2"

    exit_code=$((exit_code1+exit_code2))
    deactivate
}

clear_cache && install && ut

exit ${exit_code}