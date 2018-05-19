package cn.justin.jimu;

import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import cn.justin.cypt.CryptHelper;

public class CreditAssign {
	static Properties p = new Properties();

	static ChromeDriverService service = null;
	static ChromeDriver driver = null;


	public static void main(String[] args) throws FileNotFoundException, IOException {

		String loginURL = "https://www.jimu.com/User/Login";
		String creditAssignURL = "https://box.jimu.com/CreditAssign/List?rate=10&orderIndex=1";

		try {
			p.load(new FileReader("config.txt"));
			initDriver();
			 String username = p.getProperty("username");
			 String password = CryptHelper.decrypt(p.getProperty("password"),
			 username);
			 driver.get(loginURL);
			 driver.findElementById("username").sendKeys(username);
			 driver.findElementById("password").sendKeys(password);
			 driver.findElementById("act_login").submit();
			int count = 0;
			List<WebElement> tds = null;


			while (true) {
				driver.get(creditAssignURL);

				tds = driver.findElementsByPartialLinkText("债权转让项目");

				if (!tds.isEmpty()) {

					Collections.shuffle(tds);
					for (WebElement td : tds) {
						if ("10.5".equals(td.getText().split("\n")[4])) {
							try {
								driver.get(td.getAttribute("href"));
								driver.findElementById("act_project_all_in").click();
								driver.findElementById("act_project_invest").click();

								Thread.sleep(100);
								driver.findElementById("Contract").click();
								driver.findElementById("act_invest_confirm").click();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					Toolkit.getDefaultToolkit().beep();
				}

				System.out.println(count++);
				// 5 min
				Thread.sleep(1000 * 5 * 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (service != null)
				service.stop();
		}

	}

	private static void initDriver() throws IOException {
		String chromedriver = p.getProperty("chromedriver");
		boolean headless = Boolean.parseBoolean(p.getProperty("headless"));
		System.setProperty("webdriver.chrome.driver", chromedriver);
		service = ChromeDriverService.createDefaultService();
		service.start();
		ChromeOptions op = new ChromeOptions();
		op.setHeadless(headless);
		driver = new ChromeDriver(service, op);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

}
