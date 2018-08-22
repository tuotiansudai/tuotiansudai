import datetime
import os
from fabric.api import *

config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')

env.use_ssh_config = True
env.always_use_pty = False
env.ssh_config_path = config_path+'/config'


def build_log_path(type='web'):
    def daterange(start_date, end_date):
        for n in range(int ((end_date - start_date).days)):
            yield (start_date + datetime.timedelta(n)).strftime('%Y%m%d')

    start_day = datetime.datetime.today()
    end_day = datetime.datetime.today() - datetime.timedelta(days=8)

    return reduce(lambda s, item: '{0} /var/log/nginx/{1}.access.log-{2}.gz'.format(s, type, item),
                daterange(end_day, start_day), ' ')


def mkzip(host, type='web'):
    env.host_string = host
    zip_name = '%s.zip' % host
    with cd('/root/'):
        run('rm {0}'.format(zip_name))
        run('zip -r {0} {1}'.format(zip_name, build_log_path(type)))
        get(zip_name, local_path='./' + zip_name)


def send_mail():
    # Send the email (this example assumes SMTP authentication is required)
    import smtplib
    from os.path import basename
    from email.MIMEMultipart import MIMEMultipart
    from email.mime.application import MIMEApplication

    strFrom = 'no-reply@tuotiansudai.com'
    RECIPIENT = ["gaoxiduan@tuotiansudai.com", "zhoujinmeng@tuotiansudai.com"]

    msg = MIMEMultipart()
    msg["From"] = strFrom
    msg["To"] = ', '.join(RECIPIENT)
    msg["Subject"] = u"{0}-logs".format(datetime.datetime.today())

    for item in ('beijing.zip', 'shanghai.zip'):
        with open(item, 'rb') as f:
            part = MIMEApplication(
                    f.read(),
                    Name=basename(item)
                )
            part['Content-Disposition'] = 'attachment; filename="%s"' % basename(item)
            msg.attach(part)

    smtp = smtplib.SMTP()
    smtp.connect('smtp.qiye.163.com')
    smtp.login('no-reply@tuotiansudai.com', 'w62CQIhM6acj')
    smtp.sendmail(strFrom, RECIPIENT, msg.as_string())
    smtp.quit()


def send():
    """
    Archive recent 7 days nginx log and send them by email
    """
    def get_password():
        with open('/workspace/ci/def', 'rb') as f:
            return f.readline().strip()
    env.password = get_password()
    mkzip('beijing')
    mkzip('shanghai')
    #mkzip('wuhan', 'cms')
    send_mail()
