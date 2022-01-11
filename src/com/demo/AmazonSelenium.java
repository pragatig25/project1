package com.demo;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.Select;

public class AmazonSelenium {
	
	private static String Category;
	private static String SearchVal;



	public static void main(String[]args)   {
		//System.setProperty("webdriver.chrome.driver","home/guptapragati199/eclipse-workspace/AutomationAmazon/drivers/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.amazon.ca");
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","root");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Amazon");
			  
			
			    while (rs.next()) {
				System.out.println(rs.getString(2) + " " + rs.getString(3));
				Category = rs.getString(2);
				SearchVal = rs.getString(3);
			    
				
			    }
			      
			    WebElement weCat = driver.findElement(By.xpath("//select[@id='searchDropdownBox']"));
				
				Select slCatDrop = new Select(weCat);
				slCatDrop.selectByVisibleText(Category);

				WebElement weSearchText = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
				weSearchText.sendKeys(SearchVal);
				
				WebElement weSearchButton = driver.findElement(By.xpath("//input[@id='nav-search-submit-button']"));
				weSearchButton.click();
			
				/* milestone 4 - count search result */		
				java.util.List<WebElement> ResList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']"));
				System.out.println("Total search result are: " + ResList.size());
				
				String ResListCount = String.valueOf(ResList.size());
				
				WebElement ResultBarText = driver.findElement(By.xpath("//*[@class='a-section a-spacing-small a-spacing-top-small']/span[1]"));
				System.out.println("Result bar: " + ResultBarText.getText());
			
				String ResultCountTxt = ResultBarText.getText().substring(3, 3);
				System.out.println("ResultCountTxt = " + ResultCountTxt);
				
				if ( ResultCountTxt.equals(ResListCount)) {
					System.out.println("Result is matching");	
				}
				else {
					System.out.println(" Result is not matching");
				}
				
				
				/* milestone 5 - capture screen */		
				// Note: remember to get the io jar https://mvnrepository.com/artifact/commons-io/commons-io/2.6
				TakesScreenshot TsObj = (TakesScreenshot) driver;
				
				File myFile = TsObj.getScreenshotAs(OutputType.FILE);
				
				try {
					FileUtils.copyFile(myFile, new File("amazonsearch.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/* milestone 6 [extra]- list the price & name of the product */		
				java.util.List<WebElement> ProductNameList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']//span[@class='a-size-medium a-color-base a-text-normal']"));
				java.util.List<WebElement> ProductPriceList = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']//span[@class='a-price-whole']"));
				
				System.out.println("Product List: [" + SearchVal + "] from category [" + Category + "]"  );
				System.out.println("---------------------------------------------------------");
				for ( int i = 0; i < ProductNameList.size(); i++ ) {
					System.out.println( (i+1) + ". Rs" + ProductPriceList.get(i).getText() + " - " + ProductNameList.get(i).getText());
				}
				
				
			} catch (ClassNotFoundException e) {
					
				// e.printStackTrace();
				System.out.println("Class not found");
			} catch (SQLException e) {
				
				//e.printStackTrace();
				System.out.println("SQL Exception");
					
			}

			// close after 5 seconds
			try {
				Thread.sleep(5000); //time is in ms (1000 ms = 1 second)
				driver.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			
		
		
		}

}


		
		

	

		
		
		
	


