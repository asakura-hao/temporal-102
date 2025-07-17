package translationworkflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import translationworkflow.model.TranslationActivityInput;
import translationworkflow.model.TranslationActivityOutput;
import translationworkflow.model.TranslationWorkflowInput;
import translationworkflow.model.TranslationWorkflowOutput;
import org.slf4j.Logger;
import java.time.Duration;

public class TranslationWorkflowImpl implements TranslationWorkflow {

  // TODO: Define the Workflow logger

  public static final Logger logger = Workflow.getLogger(TranslationWorkflowImpl.class);

  private final ActivityOptions options =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofSeconds(5))
          .build();

  private final TranslationActivities activities =
      Workflow.newActivityStub(TranslationActivities.class, options);

  @Override
  public TranslationWorkflowOutput sayHelloGoodbye(TranslationWorkflowInput input) {
    String name = input.getName();
    String languageCode = input.getLanguageCode();

    // TODO: Add a log statement at the info level stating that the Workflow has been invoked
    // Be sure to include variable information
    logger.info("[Invoke] for name: {} and language code: {}", name, languageCode);

    TranslationActivityInput helloInput = new TranslationActivityInput("hello", languageCode);
    TranslationActivityOutput helloResult = activities.translateTerm(helloInput);
    String helloMessage = helloResult.getTranslation() + ", " + name;

    // Wait a little while before saying goodbye
    // TODO: Part C - Add a log statement at the info level stating "Sleeping between translation
    // calls"
    // TODO: Part C - Use Workflow.sleep to create a timer here for 30s
    logger.info("waiting for 30s");
    Workflow.sleep(Duration.ofSeconds(30));
    
    // TODO: Add a log statement here at the debug level stating that the Activity is going
    // to be invoked. Be sure to include the word being translated and the language code.

    logger.debug("Sleeping between translation calls", helloMessage);
    
    TranslationActivityInput goodbyeInput = new TranslationActivityInput("goodbye", languageCode);
    TranslationActivityOutput goodbyeResult = activities.translateTerm(goodbyeInput);
    String goodbyeMessage = goodbyeResult.getTranslation() + ", " + name;

    return new TranslationWorkflowOutput(helloMessage, goodbyeMessage);
  }
}
