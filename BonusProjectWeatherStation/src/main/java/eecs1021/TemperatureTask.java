package eecs1021;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TemperatureTask extends TimerTask{

    private	final SSD1306 display;
    private	final Pin pin;
    private final Timer timer;

    private int cityTemperature = 0;

    private ArrayList<Double> val = new ArrayList<>();

    //	class	constructor.
    public TemperatureTask(Pin temperatureSensor, SSD1306 display, Timer timer)	{
        this.display = display;
        this.pin = temperatureSensor;
        this.timer = timer;
    }

    private static double localTemperature;
    @Override
    public void run() {
        //ButtonListener obj = new ButtonListener(pin, pin, display, pin);
        String temperature = String.valueOf(pin.getValue());
        double conversion = (Double.parseDouble(temperature) / 1023)*5;
        double result = (5 - conversion) / conversion*4700;
        localTemperature = (1/(Math.log(result/10000) / 3950 + 1/(25+273.15)) - 273.15);
        localTemperature = (double) Math.round(localTemperature * 100) / 100;
        System.out.println("local temp is " + localTemperature);


        eecs1021.Main obj = new eecs1021.Main();
        int page = obj.counter;

        obj.temperatureList.add(localTemperature);

        String temp = obj.temp;
        String wind = obj.wind;

        if (page == 1) {
            Check();
            //Get temperature value from Temperature method in the CurrentWeather class and parse it to double and round it to nearest whole number and then parse it to an integer
            // System.out.println(temp);
            // System.out.println((int) Math.round(Double.parseDouble(temp)));
            int cityTemperature = ((int) Math.round(Double.parseDouble(temp)));
//        int temperature = (int) Math.round(Double.parseDouble(currentWeatherObj.Temperature()));
            String location = obj.place;
            //Clear display, put data onto the display, and update the display
            //display.clear();
            display.getCanvas().setTextsize(3);
            display.getCanvas().drawString(30, 30, Integer.toString(cityTemperature) + Character.toString((char) 248) + "C");
            display.display();
            display.getCanvas().setTextsize(1);
            display.getCanvas().drawString(35, 0, location);
            display.display();

        }

        else if (page == 2) {
            Check();
            //Get wind speed from Wind method in the CurrentWeather class and parse it to double and round it to nearest whole number and then parse it to an integer
            double cityWind = Double.parseDouble(wind);
            String location = obj.place;
            //Clear display, put data onto the display, and update the display
            // display.clear();
            display.getCanvas().setTextsize(2);
            display.getCanvas().drawString(30,30,Double.toString(cityWind) + " m/s");
            display.display();
            display.getCanvas().setTextsize(1);
            display.getCanvas().drawString(35,0, location);
            display.display();
        }
        else if (page == 3) {
            Check();

            //Display value on display
            //display.clear();
            display.getCanvas().setTextsize(2);
            display.getCanvas().drawString(30, 30, Double.toString(localTemperature) + Character.toString((char) 248) + "C         ");
            display.display();
            display.getCanvas().setTextsize(1);
            display.getCanvas().drawString(25, 0, "Local Temperature");
            display.display();

        } else {
            System.out.println("sad face");
        }






    }

    public void Check(){
        Main obj = new Main();
        if(obj.prevVal == 1){
            display.clear();
            obj.prevVal = 0;
        }
    }







}