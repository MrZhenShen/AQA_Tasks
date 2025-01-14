package testFramework.duolingoUI.po;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testFramework.duolingoUI.decorator.ButtonElement;
import testFramework.duolingoUI.decorator.InputElement;
import testFramework.duolingoUI.decorator.MyFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testFramework.duolingoUI.util.QuizType;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizPO {
    private final WebDriver driver;

    public QuizPO(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new MyFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div/div[2]/div/div/div/div/div[2]/div[1]/div/div[2]/div[1]/div/span/div")
    private List<WebElement> words;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div/div[2]/div/div/div/div/div[2]/div[2]/div/textarea")
    private InputElement answerInput;

    @FindBy(xpath = "//*[@id=\"session/PlayerFooter\"]/div/div[2]/button")
    private ButtonElement checkButton;

    @FindBy(xpath = "//*[@id=\"session/PlayerFooter\"]/div/div[1]/div")
    private WebElement taskResult;

    @FindBy(xpath = "//*[@id=\"session/PlayerFooter\"]/div/div[1]/button")
    private ButtonElement skipButton;

    @FindBy(xpath = "//*[@id=\"session/PlayerFooter\"]/div/div[2]/button")
    private ButtonElement continueButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div/div[2]/div/div/div")
    private WebElement quizWrapper;

    public String readTask() {
        getSpecificTask(QuizType.TRANSLATE);
        while (words.size() == 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        StringBuffer sentence = new StringBuffer();
        for(WebElement word : words) {
            sentence.append(word.getText()).append(" ");
        }
        System.out.println(sentence);
        return sentence.toString();
    }

    public void fillAnswer(String toText) {
        answerInput.input(toText);
    }

    public void clickCheck() {
        checkButton.click();
    }

    public String checkLanguage() {
        return answerInput.getAttribute("lang");
    }

    public boolean isAnswerCorrect() {
        return taskResult.getAttribute("data-test").equals("blame blame-correct");
    }

    public void clickSkip() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        skipButton.click();
    }

    public void clickContinue() {
        continueButton.click();
    }

    public boolean isEnableAnswerInput() {
        getSpecificTask(QuizType.TRANSLATE);
        clickSkip();
        return answerInput.isEnable();
    }

    public void getSpecificTask(QuizType type) {
        while (getQuizType() != type) {
            clickSkip();
            clickContinue();
        }
    }

    public QuizType getQuizType() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(quizWrapper))
                .isEnabled();

        String quizType = quizWrapper.getAttribute("data-test").split("-")[1];
        switch (quizType) {
            case "translate":
                return QuizType.TRANSLATE;
            case "form":
                return QuizType.FORM;
            case "name":
                return QuizType.NAME;
            default:
                System.out.println(quizType);
                return null;
        }
    }

    public boolean hasSkillDifferentQuizzes(int quizAmount) {
        Set<QuizType> uniqueQuizzes = new HashSet<>();
        for (int i = 0; i < quizAmount; i++) {
            uniqueQuizzes.add(getQuizType());
            if (uniqueQuizzes.size() == 1) {
                clickSkip();
                clickContinue();
            } else {
                return true;
            }
        }
        return false;
    }
}
