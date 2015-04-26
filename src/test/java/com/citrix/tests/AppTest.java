package com.citrix.tests;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import org.openqa.selenium.JavascriptExecutor;
import java.text.SimpleDateFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
 
public class AppTest {
 
    //The 'browser' itself
    private WebDriver driver;
    final static Properties prop = new Properties();

    @BeforeSuite
    public void init() {
        //Load property file from resource dir
        String propFileName = "datafile.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try{
            prop.load(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
 
    @BeforeTest
    public void setupSelenium(){
        //Start the browser
        System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver"));
        driver = new ChromeDriver();
        //Adds implicit timeouts to the driver
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
	
    @DataProvider(name = "test1")
    public static Object[][] primeNumbers() {
        Date date = new Date();
        String day = getDate(date);
        return new Object[][] {{"Meeting 1", "This is the first meeting", day, "9:00", "AM", "10:00", "Pacific"}};
    }
	
    private static String getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 3);
        String dt = sdf.format(c.getTime());
        String[] tokens = dt.split(" ");
        return tokens[2];
    }
 
    @Test(groups = { "login" })
    public void testLogin(){
        driver.navigate().to("https://global.gotowebinar.com/webinars.tmpl");
		
        driver.findElement(By.id("emailAddress")).clear();
        driver.findElement(By.id("emailAddress")).sendKeys(prop.getProperty("email"));
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(prop.getProperty("password"));
        driver.findElement(By.id("submit")).click();
    }
	
    @Test(dependsOnGroups = { "login" }, dataProvider = "test1")
    public void testCreateWebinar(String name, String desc, String date, String starttime, String am, String endtime, String timezone){
		
        //Click "Schedule a webinar" button
        driver.findElement(By.id("scheduleWebinar")).click();
		
        //Input meeting title and description
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("description")).sendKeys(desc);
		
        //Bring up calendar and select a date
        driver.findElement(By.className("ui-datepicker-trigger")).click();
        driver.findElement(By.xpath("//a[text()='" + date + "']")).click();

        //Set starting time
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('webinarTimesForm.dateTimes_0.startTime').setAttribute('value', '"+starttime+"')");

        //Select am/pm
        WebElement arrow = driver.findElement(By.id("webinarTimesForm_dateTimes_0_startAmPm_trig"));
        arrow.findElement(By.className("arrow")).click();
        WebElement select = driver.findElement(By.id("webinarTimesForm_dateTimes_0_startAmPm__menu"));
        List<WebElement> ele = select.findElements(By.className("ellipsis"));
        if(am.equals("AM")){
            ele.get(0).click();
        } else {
            ele.get(1).click();
        }
		
        //Set ending time
        js.executeScript("document.getElementById('webinarTimesForm.dateTimes_0.endTime').setAttribute('value', '"+endtime+"')");
		
        //Select timezone
        arrow = driver.findElement(By.id("webinarTimesForm_timeZone_trig"));
        arrow.findElement(By.className("arrow")).click();
        select = driver.findElement(By.id("webinarTimesForm_timeZone__menu"));
        List<WebElement> tzone = select.findElements(By.className("ellipsis"));
        tzone.get(0).click();
 
        //Select locale
        arrow = driver.findElement(By.id("language_trig"));
        arrow.findElement(By.className("arrow")).click();
        select = driver.findElement(By.id("language__menu"));
        List<WebElement> lang = select.findElements(By.className("ellipsis"));
        lang.get(5).click();
        
        //driver.findElement(By.id("schedule.submit.button")).click();
    }
	
    @Test(dependsOnGroups = { "login" })
    public void testExistingWebinar(){
        driver.navigate().to("https://global.gotowebinar.com/webinars.tmpl");
		
        //driver.findElement(By.linkText("/manageWebinar.tmpl?webinar=5336905562490408193")).click();
        driver.findElement(By.id("webName")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertEquals(driver.findElement(By.id("trainingName")).getText(), "Fake meeting for testing", "Wrong name");
        assertEquals(driver.findElement(By.id("trainingDesc")).getText(), "Testing meeting scheduling module.", "Wrong description");
        assertEquals(driver.findElement(By.id("dateTime")).getText(), "Mon, Apr 27, 2015 10:00 AM - 11:00 AM SST", "Wrong time");
        assertEquals(driver.findElement(By.id("locale")).getText(), "简体中文", "Wrong locale");
    }
	
 
    @AfterTest
    public void closeSelenium(){
        //Shutdown the browser
        //driver.quit();
    }
}