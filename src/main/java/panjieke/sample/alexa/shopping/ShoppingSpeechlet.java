package panjieke.sample.alexa.shopping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

public class ShoppingSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(ShoppingSpeechlet.class);
    private static final String SHOPPING_CART = "Shopping Cart";
    private static final String SHOPPING_CART_INTENT = "ShoppingCartIntent";

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {
        log.info("onSessionStarted requestId={}, sessionId={}",
                speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        log.info("onLaunch requestId={}, sessionId={}",
                speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());

        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest request = speechletRequestEnvelope.getRequest();

        log.info("onIntent requestId={}, sessionId={}",
                request.getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        return getSimpleSpeechletResponse(intentName);

    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        log.info("onSessionEnded requestId={}, sessionId={}",
                speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());
    }

    private SpeechletResponse createSimpleSpeechletResponse(String speechText) {
        SimpleCard card = new SimpleCard();
        card.setTitle(SHOPPING_CART);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getSimpleSpeechletResponse(String intentName) {
        if (SHOPPING_CART_INTENT.equals(intentName)) {
            return getOpenShoppingCartResponse();
        } else {
            return getUnknownCommandResponse();
        }
    }

    private SpeechletResponse getRepromptSpeechletResponse(String intentName) {
        if (SHOPPING_CART_INTENT.equals(intentName)) {
            return getOpenShoppingCartResponse();
        } else {
            return getUnknownCommandResponse();
        }
    }

    private SpeechletResponse getWelcomeResponse() {
        return createSimpleSpeechletResponse("Welcome to Shopping Cart");
    }

    private SpeechletResponse getOpenShoppingCartResponse() {
        return createSimpleSpeechletResponse("This is your shopping cart");
    }

    private SpeechletResponse getUnknownCommandResponse() {
        return createSimpleSpeechletResponse("Sorry, I did not understand your command");
    }
}
