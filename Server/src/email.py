import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from random import randint


def email():
    fromaddr = "checkedouthelp@gmail.com"
    toaddr = "dioz.rl@gmail.com"
    msg = MIMEMultipart()
    msg['From'] = fromaddr
    msg['To'] = toaddr
    msg['Subject'] = "Email Verification"

    num = str(random())

    # Create the body of the message (a plain-text and an HTML version).
    text = "Please type the following 6-digit code to verify your email: \n" + num + "\n"

    # Record the MIME types of both parts - text/plain and text/html.
    part1 = MIMEText(text, 'plain')

    # Attach
    msg.attach(part1)

    # Send the message via local SMTP server.
    mail = smtplib.SMTP('smtp.gmail.com', 587)

    mail.ehlo()

    mail.starttls()

    mail.login(fromaddr, 'ryangroup')
    mail.sendmail(fromaddr, toaddr, msg.as_string())
    mail.quit()


def random():
    range_start = 10**(6-1)
    range_end = (10**6)-1
    return randint(range_start, range_end)

if __name__ == "__main__":
    email()
