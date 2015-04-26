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

## License

The content of this project is licensed under the [MIT license](http://opensource.org/licenses/mit-license.php).

