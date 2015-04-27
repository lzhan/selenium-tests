package com.citrix.tests;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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
        return new Object[][] {{"Meeting 1", "This is the first meeting", day, "9:00", "AM", "10:00", "Alaska"}};
    }
	
    private static String getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 3);
        String dt = sdf.format(c.getTime());
        return dt;
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
		String[] tokens = date.split(" ");
        driver.findElement(By.className("ui-datepicker-trigger")).click();
        driver.findElement(By.xpath("//a[text()='" + tokens[2].replace(",", "") + "']")).click();

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
        tzone.get(2).click();
 
        //Select locale
        arrow = driver.findElement(By.id("language_trig"));
        arrow.findElement(By.className("arrow")).click();
        select = driver.findElement(By.id("language__menu"));
        List<WebElement> lang = select.findElements(By.className("ellipsis"));
        lang.get(5).click();
        
        driver.findElement(By.id("schedule.submit.button")).click();
    }
	
    @Test(dependsOnGroups = { "login" }, dataProvider = "test1")
    public void testCreateWebinarConf(String name, String desc, String date, String starttime, String am, String endtime, String timezone){

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertEquals(driver.findElement(By.id("trainingName")).getText(), name);
        assertEquals(driver.findElement(By.id("trainingDesc")).getText(), desc);
        assertEquals(driver.findElement(By.id("dateTime")).getText(), date + " " + starttime + " " + am + " - " + endtime + " " + am + " AKDT", "Wrong time");
        assertEquals(driver.findElement(By.id("locale")).getText(), "简体中文", "Wrong locale");

        String id = driver.findElement(By.id("WebinarInfoID")).getText();

		//Check if newly added webminar is listed in the "My Webinars" page
        driver.navigate().to("https://global.gotowebinar.com/webinars.tmpl");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		int index = 0;
		List<WebElement> listedId = driver.findElements(By.className("myWebinarDetailInfo"));
		Iterator<WebElement> it = listedId.iterator();
		while(it.hasNext()){
			String text = it.next().getText();
			if(text.contains("Webinar ID")) {
				if(text.contains(id)){
					break;
				}
				index++;
			}
		}
		List<WebElement> listedNames = driver.findElements(By.id("webName"));
        assertEquals(listedNames.get(index).getText(), name, "Wrong name");
		List<WebElement> listedDate = driver.findElements(By.className("myWebinarDate"));
        assertEquals(listedDate.get(index*2+1).getText(), date.substring(0, 11), "Wrong date");
    }
	
 
    @AfterTest
    public void closeSelenium(){
        //Shutdown the browser
        driver.quit();
    }
}