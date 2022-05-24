package testFramework.duolingoUI;

import testFramework.duolingoUI.bo.AuthBO;
import testFramework.duolingoUI.bo.DashboardBO;
import testFramework.duolingoUI.bo.QuizBO;
import testFramework.duolingoUI.util.BrowserFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import tasks.task_14.AllureListener;
import static testFramework.credentials.DuolingoCredentials.*;

@Listeners({AllureListener.class})
public class DuolingoTest {
    WebDriver driver;

    @BeforeTest
    void init() {
        driver = BrowserFactory.getSafariDriver();
    }

    @AfterTest
    void end() {
        driver.close();
    }

    @Test
    void authTest() {
        AuthBO authBO = new AuthBO(driver);

        authBO.goToDuolingo()
                .goToLogIn()
                .fillLogin(LOGIN.data)
                .fillPassword(PASSWORD.data)
                .clickLogIn()
                .validateLogIn();
    }

    @Test
    void findQuizTest() {
        AuthBO authBO = new AuthBO(driver);

        authBO.goToDuolingo()
                .goToLogIn()
                .fillLogin(LOGIN.data)
                .fillPassword(PASSWORD.data)
                .clickLogIn();

        DashboardBO dashboardBO = new DashboardBO(driver);
        dashboardBO.printQuizzes()
                .validateQuizExistence("Basics 1");
    }

    @Test
    void languageQuizTest() {
        AuthBO authBO = new AuthBO(driver);

        authBO.goToDuolingo()
                .goToLogIn()
                .fillLogin(LOGIN.data)
                .fillPassword(PASSWORD.data)
                .clickLogIn();

        DashboardBO dashboardBO = new DashboardBO(driver);
        dashboardBO
                .initDashboardPage()
                .selectQuiz("Basics 1")
                .takeQuiz();

        QuizBO quizBO = new QuizBO(driver);
        quizBO
                .initQuizPage()
                .readTask()
                .checkLanguage()
                .translate()
                .fillAnswer()
                .submitAnswer()
                .validateCorrectAnswer();
    }

    @Test
    void isEnableAnswerInputAfterSkipTest() {
        AuthBO authBO = new AuthBO(driver);

        authBO.goToDuolingo()
                .goToLogIn()
                .fillLogin(LOGIN.data)
                .fillPassword(PASSWORD.data)
                .clickLogIn();

        DashboardBO dashboardBO = new DashboardBO(driver);
        dashboardBO
                .initDashboardPage()
                .selectQuiz("Basics 1")
                .takeQuiz();

        QuizBO quizBO = new QuizBO(driver);
        quizBO
                .initQuizPage()
                .skipTask()
                .validateIsEnableAnswerInput();
    }
}