import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from random import randint


def email(client_email):
    fromaddr = "checkedouthelp@gmail.com"
    toaddr = client_email
    msg = MIMEMultipart()
    msg['From'] = fromaddr
    msg['To'] = toaddr
    msg['Subject'] = "Email Verification"

    # num = str(random_6())

    num = str(666666)
    greeting = "Hello and welcome to CheckedOut! \n"
    auth = "Please type the following 6-digit code to verify your email: \n" + num + "\n"

    part1 = MIMEText(greeting, 'plain')
    part2 = MIMEText(auth, 'plain')

    # Attach
    msg.attach(part1)
    msg.attach(part2)

    # Send the message via local SMTP server.
    mail = smtplib.SMTP('smtp.gmail.com', 587)

    mail.ehlo()

    mail.starttls()

    mail.login(fromaddr, 'ryangroup')
    mail.sendmail(fromaddr, toaddr, msg.as_string())
    mail.quit()

    return num


def random_6():
    range_start = 10**(6-1)
    range_end = (10**6)-1
    return randint(range_start, range_end)
