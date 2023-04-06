import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ApplicationForDebitCardTest {
    WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldCheckPositive() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга Александрова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldCheckLastNameWithHyphen() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга Александрова-Ефремова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldCheckLastNameAndNameWithHyphen() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга-Инна Александрова-Ефремова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldNegativeCheckWrongName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Olga Aleksandrova");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldNegativeCheckEmptyFieldName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldNegativeCheckEmptyFieldPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга Александрова");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldNegativeCheckExceptAgreement() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга Александрова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001112233");
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim();
        assertEquals(expected, actual);

    }

    @Test
    void shouldCheckNegativeWithLongPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ольга Александрова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7900111223344");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);

    }
}
