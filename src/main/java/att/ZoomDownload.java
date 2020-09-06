package att;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZoomDownload {

    public static void main(String[] args) throws InterruptedException {

        try {
            System.setProperty("webdriver.chrome.driver", args[2]);
            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.get(args[3]); //zoom link
            driver.manage().window().maximize();

            driver.findElement(new By.ById("identification")).sendKeys(args[4]); //username
            driver.findElement(new By.ById("authn-go-button")).click();
            driver.findElement(new By.ById("ember517")).sendKeys(args[5]); // password
            driver.findElement(new By.ById("authn-go-button")).click();
            Thread.sleep(1000);

            driver.get(args[3]); // zoom report link
            Thread.sleep(3000);

            List<WebElement> webElementList = driver.findElements(new By.ByClassName("col6"));
            String linkText = webElementList.get(webElementList.size() - 2).getText();
            System.out.println(linkText);
            driver.findElement(new By.ByLinkText(linkText)).click();
            driver.findElement(new By.ById("selectUnique")).click();
            Thread.sleep(1000);
            driver.findElement(new By.ById("btnExportParticipants")).click();

            driver.get(args[6]);
            driver.findElement(By.className("auth__providerButton")).click();
            Thread.sleep(1000);
            driver.get(args[6]); // skyward link

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
