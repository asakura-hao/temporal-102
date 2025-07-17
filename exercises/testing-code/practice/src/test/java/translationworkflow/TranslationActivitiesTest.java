package translationworkflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.temporal.failure.ActivityFailure;
import io.temporal.testing.TestActivityEnvironment;
import translationworkflow.TranslationActivities;
import translationworkflow.TranslationActivitiesImpl;
import translationworkflow.model.TranslationActivityInput;
import translationworkflow.model.TranslationActivityOutput;

public class TranslationActivitiesTest {

  private TestActivityEnvironment testEnvironment;
  private TranslationActivities activity;

  @BeforeEach
  public void init() {
    testEnvironment = TestActivityEnvironment.newInstance();
    testEnvironment.registerActivitiesImplementations(new TranslationActivitiesImpl());
    activity = testEnvironment.newActivityStub(TranslationActivities.class);
  }

  @AfterEach
  public void destroy() {
    testEnvironment.close();
  }

  @Test
  public void testSuccessfulTranslateActivityHelloGerman() {
    TranslationActivityInput input = new TranslationActivityInput("hello", "de");
    TranslationActivityOutput output = activity.translateTerm(input);
    assertEquals("Hallo", output.getTranslation());
  }

  // TODO: Part B - Add the test case for testSuccessfulTranslateActivityGoodbyeLatvian
  @Test
  public void testSuccessfulTranslateActivityGoodbyeLatvian() {
    TranslationActivityInput input = new TranslationActivityInput("goodbye", "lv");
    TranslationActivityOutput output = activity.translateTerm(input);
    assertEquals("Ardievu", output.getTranslation());
  }
  

  // TODO: Part C - Add the code snippet for testing testFailedTranslateActivityBadLanguageCode

   @Test
  public void testFailedTranslateActivityBadLanguageCode() {
     TranslationActivityInput input = new TranslationActivityInput("goodbye", "xq");

     // Assert that an error was thrown and it was an Activity Failure
     Exception exception = assertThrows(ActivityFailure.class, () -> {
        TranslationActivityOutput output = activity.translateTerm(input);
     });

     // Assert that the error has the expected message, which identifies
     // the invalid language code as the cause
     assertTrue(exception.getMessage().contains(
         "Invalid language code"),
         "expected error message");
  }
}
