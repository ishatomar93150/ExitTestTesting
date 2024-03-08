import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CartTests extends FlipkartTests {

    private Object wait;

    @BeforeTest
    public void setUp() {
        wait = new WebDriverWait(driver, 15); // Initialize WebDriverWait
    }

    @Test
    public void addToCartFunctionality_Positive() {
        WebElement addToCartButton = driver.findElement(By.xpath(addToCartXPath));
        addToCartButton.click();
        WebElement cartIcon = ((FluentWait<WebDriver>) wait).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cartIconXPath)));
        Assert.assertTrue(cartIcon.isDisplayed(), "Item was not added to the cart successfully");
    }
    
    @Test
    public void addToCartFunctionality_Negative() {
        String outOfStockButtonXPath = "//button[contains(text(),'Out of Stock')]"; // Define the XPath here
        // Simulate adding an out-of-stock item to cart
        WebElement outOfStockButton = driver.findElement(By.xpath(outOfStockButtonXPath));
        outOfStockButton.click();
        WebElement outOfStockMessage = ((FluentWait<WebDriver>) wait).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(outOfStockButtonXPath)));
        Assert.assertTrue(outOfStockMessage.isDisplayed(), "Out of stock message not displayed");
    }
    
    @Test
    public void removeFromCartFunctionality() {
        // Implement the functionality to remove item from cart
        WebElement cartItem = driver.findElement(By.xpath("//div[@class='cart-item']")); // Locate the cart item
        WebElement removeButton = cartItem.findElement(By.xpath(".//button[contains(text(),'Remove')]")); // Locate the remove button within the cart item
        removeButton.click(); // Click on the remove button

        // Wait for the cart item to be removed
        ((FluentWait<WebDriver>) wait).until(ExpectedConditions.invisibilityOf(cartItem));

        // Assert that the item has been removed from the cart
        Assert.assertTrue(driver.findElements(By.xpath("//div[@class='cart-item']")).isEmpty(), "Item was not removed from the cart successfully");
    }
}
