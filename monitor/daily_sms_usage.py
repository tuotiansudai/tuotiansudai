# -*- coding: utf-8 -*-
import datetime
import requests
import logging


logger = logging.getLogger(__name__)
hdlr = logging.FileHandler('/var/log/sms.log')
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.INFO)


def format_today():
    return datetime.datetime.today().strftime("%Y-%m-%d")

DB_NAME = "/workspace/monitor.db"
PNG_FILE = "/tmp/{0}.png".format(format_today())
RECIPIENT = 'all@tuotiansudai.com'


def get_sn_and_pwd():
    try:
        config_file = open('/workspace/production_config/zucp_sms_config.properties', 'r')
        sn, pwd = config_file.readlines()
        sn = sn.split('=')[1]
        pwd = pwd.split('=')[1]
        return sn, pwd
    except IOError:
        return "", ""


def get_balance():
    balance_url = 'http://sdk2.entinfo.cn:8060/webservice.asmx/GetBalance'
    sn, pwd = get_sn_and_pwd()
    if sn and pwd:
        response = requests.get(balance_url, params={'sn': sn, 'pwd': pwd})
        import xml.etree.ElementTree as ET

        return int(ET.fromstring(response.content).text.strip())
    return -1


def build_coordinates(records):
    last_balance = records[0][0]
    y_axis, x_labels = [], []
    for balance, date in records:
        if balance < 0:
            y_axis.append(balance)
        else:
            y_axis.append(last_balance - balance)
        x_labels.append(date)
        last_balance = balance
    return x_labels, y_axis


def save_and_fetch_all_usages(count):
    import sqlite3

    conn = sqlite3.connect(DB_NAME)
    cur = conn.cursor()
    today = format_today()
    cur.execute("SELECT * FROM sms_usage WHERE date=?", (today, ))
    if cur.fetchone():
        cur.execute("UPDATE sms_usage SET count=? WHERE date=?", (count, today))
    else:
        cur.execute("INSERT INTO sms_usage VALUES (?, ?)", (count, today))
    conn.commit()
    cur.execute("SELECT * FROM sms_usage ORDER BY date")
    records = cur.fetchall()
    conn.close()
    return records


def draw_png(x_labels, y_axis):
    import matplotlib.pyplot as plt
    from numpy import arange

    plt.title('SMS Daily Consumption Trend')
    plt.ylabel('Count')
    plt.grid(True)
    plt.plot(y_axis)
    plt.xticks(arange(len(x_labels)), x_labels, rotation='vertical')
    plt.savefig(PNG_FILE)


def send_mail(balance):
    # Send an HTML email with an embedded image and a plain text message for
    # email clients that don't want to display the HTML.

    from email.MIMEMultipart import MIMEMultipart
    from email.MIMEText import MIMEText
    from email.MIMEImage import MIMEImage
    import uuid

    # Define these once; use them twice!
    strFrom = 'no-reply@tuotiansudai.com'
    img_id = uuid.uuid1()

    # Create the root message and fill in the from, to, and subject headers
    msgRoot = MIMEMultipart('related')
    msgRoot['Subject'] = '每日短信使用报告:{0} 剩余短信数量:{1}'.format(format_today(), balance)
    msgRoot['From'] = strFrom
    msgRoot['To'] = RECIPIENT
    msgRoot.preamble = 'This is a multi-part message in MIME format.'

    # Encapsulate the plain and HTML versions of the message body in an
    # 'alternative' part, so message agents can decide which they want to display.
    msgAlternative = MIMEMultipart('alternative')
    msgRoot.attach(msgAlternative)

    msgText = MIMEText('This is the alternative plain text message.')
    msgAlternative.attach(msgText)

    # We reference the image in the IMG SRC attribute by the ID we give it below
    msgText = MIMEText('Balance: <b>{0}</b><br><img src="cid:{1}"><br>'.format(balance, img_id), 'html')
    msgAlternative.attach(msgText)

    # This example assumes the image is in the current directory
    fp = open(PNG_FILE, 'rb')
    msgImage = MIMEImage(fp.read())
    fp.close()

    # Define the image's ID as referenced above
    msgImage.add_header('Content-ID', '<{0}>'.format(img_id))
    msgRoot.attach(msgImage)

    # Send the email (this example assumes SMTP authentication is required)
    import smtplib
    smtp = smtplib.SMTP()
    smtp.connect('smtp.exmail.qq.com')
    smtp.login('no-reply@tuotiansudai.com', 'w62CQIhM6acj')
    smtp.sendmail(strFrom, RECIPIENT, msgRoot.as_string())
    smtp.quit()

if __name__ == "__main__":
    logger.info("Start analyzing..")
    try:
        balance = get_balance()
        logger.info("Balance:%s" % balance)
        records = save_and_fetch_all_usages(balance)
        logger.info(records)
        x_labels, y_axis = build_coordinates(records)
        logger.info(x_labels, y_axis)
        draw_png(x_labels, y_axis)
        send_mail(balance)
        logger.info("Done.")
    except Exception as e:
        logger.exception("wrong")
