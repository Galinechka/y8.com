package com;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class y8 {

	  private WebDriver driver;
	  private StringBuffer verificationErrors = new StringBuffer();
	  private String baseUrl;
	  private ArrayList<Integer> heightList=new ArrayList<Integer>();
	  private ArrayList<Integer> widthList=new ArrayList<Integer>();
	  private ArrayList<String> idList= new ArrayList<String>();
	  private String way=null;
	  private int pageNumber=1;
	  private boolean wrongSizeFound=false;
	  private boolean hasNextPage=false;

	  @Before
	  public void setUp() throws Exception {
		driver = new FirefoxDriver();
	    baseUrl = "http://ru.y8.com/tags/Addition";
	    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	  }

	  @Test
	  public void testGoogleSearch() throws Exception {
		  driver.get(baseUrl + "/");
	     do {
	    getListOfElements();
		checkForSize(heightList);
		checkForSize(widthList);
		nextPage();
	     } while (hasNextPage);
	    if (!wrongSizeFound) System.out.println("All pictures are the same size!");
	  }
	  
	  public void getListOfElements() {
		  heightList.clear();
		  widthList.clear();
		  idList.clear();
		  for ( int i=1; i<65; i++){
			  way="//div[@id='items_container']/div[" +i + "]/a/img";
			  String idWay="//div[@id='items_container']/div[" +i + "]";
			  try {
				  heightList.add(driver.findElement(By.xpath(way)).getSize().getHeight());
				   widthList.add(driver.findElement(By.xpath(way)).getSize().getWidth());
				   idList.add(driver.findElement(By.xpath(idWay)).getAttribute("id"));
			  } catch (NoSuchElementException e) { 
				  if (!wrongSizeFound) System.out.println("All pictures are the same size!");
				  i=65;
			  }
		    }  
	  }
	  
	  public boolean nextPage(){
		  try {
		  hasNextPage=driver.findElement(By.xpath("//li[@class='head']/a[last()-1]")).getText().contains(".*Next.*");
		  driver.findElement(By.xpath("//li[@class='head']/a[last()-1]")).click();
		   pageNumber++;
		  } catch (NoSuchElementException e){
			  hasNextPage=false;
		  }
		   return hasNextPage;
	  }
	  
	  public void checkForSize(ArrayList<Integer> listOfNumbers){
		for (int i=0; i<listOfNumbers.size(); i++){
			if (!listOfNumbers.get(0).equals(listOfNumbers.get(i))){
				System.out.println("Wrong size of image on a page " +
						+ pageNumber +" in row: " + (Math.round(i/4)+1) + " column: " +
						+ (i-((Math.round(i/4))*4)+1) + " item's ID " +idList.get(i));
				wrongSizeFound=true;
			}
		}
	  }
	  
	  
	  @After
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
	  }

