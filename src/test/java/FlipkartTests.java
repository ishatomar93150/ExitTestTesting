import com.first.framework.ConfigResource;
import com.first.framework.XpathResources;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public class FlipkartTests extends ConfigResource implements XpathResources {
    private static Logger logger;
    protected static WebDriver driver;
    private static ConfigResource ref;
    private WebDriverWait wait;

    public FlipkartTests() {
        logger = Logger.getLogger(FlipkartTests.class.getName());
        ref = new ConfigResource();
        if(getBrowserName().equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }else if(getBrowserName().equals("firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver=new FirefoxDriver();
        }
        driver.manage().window().maximize();
    }


    @BeforeTest
    public void createDriver() throws IOException {
        FlipkartTests obj = new FlipkartTests();
        driver.get(obj.getUrlValue());
        logger.fine("Driver instantiated Successfully");
        wait = new WebDriverWait(driver, 15);
        WebElement popUp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(popUpUserID)));
        Assert.assertTrue(popUp.isDisplayed(), "Worked");
    }

    @BeforeClass
    public void loginFunctionality() {
        try {
            WebElement loginID = driver.findElement(By.xpath(popUpUserID));
            loginID.sendKeys(ref.getUserId());
            WebElement pwd = driver.findElement(By.xpath(popUpPassword));
            pwd.sendKeys(ref.getPasswordValue());
            WebElement submit = driver.findElement(By.xpath(popUpLoginButton));
            submit.click();
            wait.until(ExpectedConditions.invisibilityOf(submit));
        } catch (ElementNotVisibleException e) {
            logger.info("Different Element is present");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Assert.assertTrue(driver.findElement(By.xpath(search)).isDisplayed());
        }
    }


    @Test
    public void searchItemFunctionality() {
        SoftAssert softAssert = new SoftAssert();
        WebElement searchField = driver.findElement(By.xpath(search));
        searchField.sendKeys(ref.getProductName());
        WebElement product = driver.findElement(productXpath);
        softAssert.assertTrue(product.isDisplayed());
        searchField.submit();
        softAssert.assertAll();
    }


    @Test
    public void itemSelectionFunctionality() {
        WebElement shirt = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(oneTshirt)));
        shirt.click();
        String Parent = driver.getWindowHandle();
        Set<String> winHandle = driver.getWindowHandles();
        for (String s : winHandle) {
            if (!s.equals(Parent))
                driver.switchTo().window(s);
            logger.info(s);
        }
        wait.until(ExpectedConditions.invisibilityOf(shirt));
    }
    @Test
    public void productDetailsFunctionality() {
       
        WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(oneTshirt)));
        product.click();

        WebElement productName = driver.findElement(By.xpath("//h1[@class='product-title']"));
        WebElement productPrice = driver.findElement(By.xpath("//div[@class='price']"));
        WebElement addToCartButton = driver.findElement(By.xpath("//button[contains(text(),'Add to Cart')]"));

        Assert.assertTrue(productName.isDisplayed(), "Product name is not displayed");
        Assert.assertTrue(productPrice.isDisplayed(), "Product price is not displayed");
        Assert.assertTrue(addToCartButton.isDisplayed(), "Add to cart button is not displayed");

    }

    // @Test
    public void purchaseFunctionality() {
        logger.info("Will write later");
    }

    @Override
    @Test
    public void enterPin() {
        logger.info("PIN is 201009");
    }

    @AfterClass
    public static void tearDown() {

        driver.quit();
    }

}
