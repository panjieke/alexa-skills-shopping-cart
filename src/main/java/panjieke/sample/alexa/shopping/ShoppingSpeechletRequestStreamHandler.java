package panjieke.sample.alexa.shopping;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class ShoppingSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds = new HashSet<String>();

    static {
        String appId = System.getenv("APP_ID");
        supportedApplicationIds.add(appId);
    }

    public ShoppingSpeechletRequestStreamHandler() {
        super(new ShoppingSpeechlet(), supportedApplicationIds);
    }
}
