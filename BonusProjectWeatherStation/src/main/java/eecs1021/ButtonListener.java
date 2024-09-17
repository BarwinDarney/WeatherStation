package eecs1021;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.util.Timer;



public class ButtonListener implements IODeviceEventListener {

    private final Pin button;
    private final Pin temperatureSensor;
    private final SSD1306 display;
    private final Pin touchSensor;


    // Constructor
    ButtonListener(Pin button, Pin temperatureSensor, SSD1306 display, Pin touchSensor) {
        this.button = button;
        this.temperatureSensor = temperatureSensor;
        this.display = display;
        this.touchSensor = touchSensor;
    }


    eecs1021.Main mainObj = new eecs1021.Main();
    private int counter = mainObj.counter;
    private boolean firstRun = true;

    private int prevState = 100;

    @Override
    public void onPinChange(IOEvent event) {

        //Initialize variables
        int prevVal = 0;
        Timer timer = new Timer();

        //Create object of DisplayTask class and TemperatureTask class
        eecs1021.WeatherTask displayTaskObj = new eecs1021.WeatherTask();
        TemperatureTask temperatureTaskObj = new TemperatureTask(button, display, timer);


        if (event.getPin().getValue() == button.getValue()) {
            System.out.println("btn val is " + button.getValue());
            //  System.out.println("event pin value is " + event.getPin().getValue());
            System.out.println(button.getValue());
            if (button.getValue() == 0) {
                counter++;
                prevState = 0;
                if (counter >= 4) {
                    counter = 1;
                }

                mainObj.counter = counter;
                System.out.println("local counter is " + counter);
                System.out.println("public counter is " + mainObj.counter);
                mainObj.prevVal = 1;


                System.out.println("Counter is: " + counter);
            } else if (button.getValue() == 1 && firstRun) {
                counter++;
                firstRun = false;
                mainObj.counter = counter;
                mainObj.prevVal = 1;
                System.out.println("this counter is " + counter);

            }


            else if (touchSensor.getValue() == 1) {
                SpeechToText();
            }
            else {
                // System.out.println("No buttons are pushed");
            }

        }

    }

    @Override
    public void onStart(IOEvent event) {}
    @Override
    public void onStop(IOEvent event) {}
    @Override
    public void onMessageReceive(IOEvent event, String message) {}


    private static String subscriptionKey = "9cbc886f9a764a3d8933f9857e5f2646";
    private static String serviceRegion = "eastus";

    private static String output;
    public void SpeechToText(){


        try {
            AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();

            SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
            SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

            System.out.println("Recognizing...");
            SpeechRecognitionResult result = recognizer.recognizeOnceAsync().get();
            output = result.getText().replace(".", "");
            //System.out.println("Recognized text: " + result.getText());
            if(touchSensor.getValue() == 0) {
                recognizer.close();
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        mainObj.place = output;
        System.out.println("boolean val " + mainObj.newCity);
        mainObj.newCity = true;
        mainObj.prevVal = 1;

    }


}












