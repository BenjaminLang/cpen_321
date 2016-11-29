# CheckedOut

## **Synopsis**

A Web and Android application which allows its users to search for items, and create shopping lists. The items searched will be prioritized by lowest price. The hope is to help save the user money by providing a fast and simple approach to determining which store provides the cheapest price for the desired item.

## **Installation**  **Guide**

Because the installation for Windows and Ubuntu are very similar, below is an installation guide for both versions. However, please make sure to download and install the correct version of each dependency.

- Install the following dependencies:
  - Python3.5.2
  - MongoDB
  - PyMongo
  - bson
  - BeautifulSoup4
  - Locust.io
  - Node.js
  - Npm
  - Android Studio
  - Picasso
- Pull the latest version of the master branch
- Run MongoDB on the terminal with &quot;mongod --config &quot;PATH\_TO\_CONFIG&quot;&quot;
  - Config is located at Server/docs/mongod\_ver.cfg
  - Use mongod\_win.cfg if on a Windows machine
  - Use mongod\_u.cfg if on an Ubuntu based machine
- To run the different tests:
  - Main Server
    - Run the main server with &quot;python3 Server/src/main.py&quot;
    - Run the various tests with:
      - &quot;python3 Server/test/list\_test.py&quot;
      - &quot;python3 Server/test/read\_write\_test.py&quot;
      - &quot;python3 Server/test/user\_test.py&quot;
    - To run the &quot;stress\_test.py&quot;, refer to Locust.io documentation
      - [http://docs.locust.io/en/latest/running-locust-distributed.html](http://docs.locust.io/en/latest/running-locust-distributed.html)
  - Webserver
    - In Web/, run &quot;npm install&quot; to install Node.js dependencies
    - Run &quot;npm run postinstall&quot; to configure the testing environment
    - Run all tests with: &quot;npm run tests&quot;
  - Android
    - Run all JUnit tests

## **API Reference**

- Google Places
- Google Maps
- PyMongo
- BeautifulSoup
- Locust

## **Tests**

Main Server:

- stress\_test.py - stress testing source file for Locust Framework
- list\_test.py - list functionality test suite for main server
- read\_write\_test.py - read/write functionality test suite for main server
- user\_test.py - user operations functionality test for main server

Webserver:

- all tests in Web/tests

Android:

- all tests in Android/tests folder

## **Contributors**

#### **Web Development (Front End)**: Leo Belanger

#### **Web Development (Back End):** Andy Wong

#### **Database, Server, and Crawler:** Ben Lang, Ryan Liu, Amrit Kooner

#### **Android:** John Sun

## **License**

See LICENSE.txt
