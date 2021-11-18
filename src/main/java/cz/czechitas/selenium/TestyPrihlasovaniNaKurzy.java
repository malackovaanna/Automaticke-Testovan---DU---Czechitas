package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    public static final String URL_DOMU = "https://cz-test-jedna.herokuapp.com/";
    WebDriver prohlizec;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void rodicSExistujicimUctemSeMusiBytSchopenPrihlasit () {

        prejdiNaPrihlaseni();

        prihlasUzivateleVaclavVomacka();

        Assertions.assertEquals("Vaclav Vomacka", najdiPrihlasenehoUzivatele("Vaclav Vomacka"), "Vaclav neni prihlasen.");

        odhlasPrihlasenehoUzivatele("Vaclav Vomacka");
    }

    @Test
    public void rodicSExistujicimUctemMusiBytSchopenVybratKurzPrihlasitSeADiteNaVybranyKurzPrihlasit () {

        prejdiNaHlavniStranku();

        vyberKurz();

        prihlasUzivateleVaclavVomacka();

        vyplnPrihlasku("Nina", "Vomackova", "11.11.2011");

        prejdiNaPrihlasky();

        Assertions.assertEquals("Nina Vomackova", najdiPrihlaseneDite(), "Nina neni prihlasena.");

        odhlasUcastDiteteNaKurzu();

        odhlasPrihlasenehoUzivatele("Vaclav Vomacka");

    }

    @Test
    public void rodicSExistujicimUctemSeMusiBytSchopenPrihlasitVyhledatKurzADiteNaNejPrihlasit () {

        prejdiNaPrihlaseni();

        prihlasUzivateleVaclavVomacka();

        prejdiNaHlavniStranku();

        vyberKurz();

        vyplnPrihlasku("Nina", "Vomackova", "11.11.2011");

        prejdiNaPrihlasky();

        Assertions.assertEquals("Nina Vomackova", najdiPrihlaseneDite(), "Nina neni prihlasena.");

        odhlasUcastDiteteNaKurzu();

        odhlasPrihlasenehoUzivatele("Vaclav Vomacka");
    }

    @Test
    public void novyUzivatelSeMusiBySchopenRegistrovat () {

        prejdiNaRegistraci();

        vyplnRegistracniFormular("Michael Vomacka", "michael.vomacka@seznam.cz", "Vomacka9");

        Assertions.assertEquals("Michael Vomacka", najdiPrihlasenehoUzivatele("Michael Vomacka"), "Nove registrovany uzivatel neni prihlasen.");

        odhlasPrihlasenehoUzivatele("Michael Vomacka");
    }

    @AfterEach
    public void tearDown() {

        prohlizec.quit();
    }

    private void vyplnRegistracniFormular(String jmenoPrijmeni, String email, String heslo) {
        vyplnPole("//input[@id='name']", jmenoPrijmeni);
        vyplnPole("//input[@id='email']", email);
        vyplnPole("//input[@id='password']", heslo);
        vyplnPole("//input[@id='password-confirm']", heslo);
        klikniNaElement("//button[@type='submit']");
    }

    private String najdiPrihlasenehoUzivatele (String jmenoPrijmeni) {
        WebElement prihlasenyUzivatel = najdiWebElement("//strong[contains(text(),'" + jmenoPrijmeni + "')]");
        return prihlasenyUzivatel.getText();
    }

    private String najdiPrihlaseneDite () {
        WebElement prihlaseneDite = najdiWebElement("//tr[@class='even' or @class='odd']/td[text()='Nina Vomackova']");
        return prihlaseneDite.getText();
    }

    private void odhlasUcastDiteteNaKurzu() {
        klikniNaElement("//a[text()='Odhlášení účasti']");
        klikniNaElement("//label[text()='Nemoc']");
        klikniNaElement("//input[@value='Odhlásit žáka']");
    }

    private void vyberKurz() {
        klikniNaElement("//a[contains(@href,'trimesicni-kurzy-webu')]");
        klikniNaElement("//a[@href='https://cz-test-jedna.herokuapp.com/zaci/pridat/41-html-1']");
    }

    private void vyplnPrihlasku(String jmeno, String prijmeni, String datumNarozeni) {
        klikniNaElement("//button[@data-id='term_id']");
        WebElement poleNaTermin = najdiWebElement("//input[@role='combobox']");
        poleNaTermin.sendKeys("01");
        poleNaTermin.sendKeys("\n");
        vyplnPole("//input[@id='forename']", jmeno);
        vyplnPole("//input[@id='surname']", prijmeni);
        vyplnPole("//input[@id='birthday']", datumNarozeni);
        klikniNaElement("//label[text()='Bankovní převod']");
        klikniNaElement("//label[@for='terms_conditions']");
        klikniNaElement("//input[@type='submit']");
    }

    private void prejdiNaPrihlasky() {
        prohlizec.navigate().to(URL_DOMU + "zaci");
    }

    private void prejdiNaRegistraci () {
        prohlizec.navigate().to(URL_DOMU + "registrace");
    }

    private void prihlasUzivateleVaclavVomacka() {
        vyplnEmail("v.vomacka@seznam.cz");
        vyplnHeslo("Karolina9");
        klikniNaElement("//button[contains(text(),'Přihlásit')]");
    }

    private WebElement najdiWebElement(String xpath) {
        WebElement webElement = prohlizec.findElement(By.xpath(xpath));
        return webElement;
    }

    private void klikniNaElement (String xpath) {
        WebElement tlacitko = prohlizec.findElement(By.xpath(xpath));
        tlacitko.click();
    }

    private void prejdiNaPrihlaseni () {
        prohlizec.navigate().to(URL_DOMU + "prihlaseni");
    }

    private void prejdiNaHlavniStranku (){
        prohlizec.navigate().to(URL_DOMU);
    }

    private void vyplnEmail (String email) {
        vyplnPole("//input[@id='email']", email);
    }

    private void vyplnHeslo (String heslo) {
        vyplnPole("//input[@id='password']", heslo);
    }

    private void vyplnPole(String xpath, String hodnotaPole) {
        WebElement email = prohlizec.findElement(By.xpath(xpath));
        email.sendKeys(hodnotaPole);
    }

    private void odhlasPrihlasenehoUzivatele (String uzivatel) {
        try {
            WebElement toaster = najdiWebElement("//div[@id='toast-container']");
            if(toaster.isDisplayed()) {
                ((JavascriptExecutor) prohlizec).executeScript("arguments[0].style.visibility='hidden'", toaster);
            }
        }  catch (NoSuchElementException e)   {
                    //toaster se zobrazuje jen nekdy
        }
        klikniNaElement("//strong[contains(text(),'" + uzivatel + "')]");
        klikniNaElement("//a[@id='logout-link']");

    }


}
