GoToWebinar Web UI Tests
======================

This is a funtional test suite to test GoToWebinar Web UI.


## Environment Setup

* Download and install [JDK 1.8.0](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Set/export JAVA_HOME
* Verify JDK installation `java -version`
* Download and install [maven 3.3.1](https://maven.apache.org/download.cgi)
* Add Maven bin directory location to system path
* Verify Maven installation `mvn -verison`
* Download [ChromeDriver](http://chromedriver.storage.googleapis.com/index.html?path=2.15/)

## Installation

* `$ git clone git@github.com:lzhan/selenium-tests.git`
* Update chromedriver path in ./src/test/resources/datafile.properties
* `mvn integration-test`

## Function tested

* Login
* Create new webinar
* Newly created webinar is listed in "My Webinars" page correctly

## Functions to be tested:

* Login:
  * invalid email address
  * invalid password
  * missing email address
  * invalid password
* Title:
  * empty title
  * long title
  * Forbidden chars
* Description:
  * empty description
  * long description
* Different type of Webinar: Series, Sequence
* Different Starting time:
  * Use default value
  * Starting time in the morning, end time is in the afternoon
  * Starting Date is not in current month: change month, year
  * Starting date is not valid: 25:00, 1200
  * Starting date is passed
* Different languages
* If newly created webinar is inserted in the right place.
* Edit existing webinar
  * Title & Description
  * Date,Time locale
  * Audio
  * Branding and themes
  * Co-organizers
  * Panelists
  * Registration Settings
  * Tracking Registrants
  * Emails, Confirmation, Follow-up
  * Recordings
  * Polls
  * Survey
* Sharing
* Copy
* Delete
* Schedule Similar Webinar
* Add to Calendar
* Start
* "24/7 Support" section
* "Training Videos" section
* "Join the GoToWebinar Community" section




## License

The content of this project is licensed under the [MIT license](http://opensource.org/licenses/mit-license.php).

