package panjieke.sample.alexa.shopping;

import com.amazon.speech.ui.Reprompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import java.util.HashMap;
import java.util.Map;

public class ShoppingSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(ShoppingSpeechlet.class);
    private static final String SHOPPING_CART = "Shopping Cart";
    private static final String SHOPPING_CART_INTENT = "ShoppingCartIntent";
    private static final String STOP_INTENT = "AMAZON.StopIntent";
    private Map<String, Integer> shoppingCart = new HashMap<String, Integer>(){{ put("banana", 3); put("apple", 5); }};

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

        if (SHOPPING_CART_INTENT.equals(intentName)) {
            return getRepromptSpeechletResponse(getMyShoppingList(), "Do you want to add something else?");
        } else if (STOP_INTENT.equals(intentName)) {
            return getGoodByeResponse();
        } else {
            return getUnknownCommandResponse();
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        log.info("onSessionEnded requestId={}, sessionId={}",
                speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());
    }

    private SimpleCard createCard(String title, String speechText) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(speechText);

        return card;
    }

    private PlainTextOutputSpeech createSpeech(String speechText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return speech;
    }

    private SpeechletResponse getRepromptSpeechletResponse(String speechText, String repromptText) {
        SimpleCard card = createCard(SHOPPING_CART, speechText);
        PlainTextOutputSpeech speech = createSpeech(speechText);

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getWelcomeResponse() {
        return getRepromptSpeechletResponse("Welcome to Shopping Cart", "You can ask for your shopping list or add items");
    }

    private String getMyShoppingList() {
        StringBuilder list = new StringBuilder().append("This is your usual shopping list:");
        for (String article : shoppingCart.keySet()) {
            list.append(shoppingCart.get(article) + " " + article + " ");
        }

        list.append("Do you want to add something else?");

        return list.toString();
    }

    private SpeechletResponse getSimpleSpeechletResponse(String speechText) {
        SimpleCard card = new SimpleCard();
        card.setTitle(SHOPPING_CART);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getGoodByeResponse() {
        return getSimpleSpeechletResponse("ByeBye");
    }

    private SpeechletResponse getUnknownCommandResponse() {
        return getSimpleSpeechletResponse("I did not understand your command");
    }
}
