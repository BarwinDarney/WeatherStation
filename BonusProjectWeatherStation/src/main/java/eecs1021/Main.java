package eecs1021;
//imports
import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException
    {

        Arduino();

    }

    //Initialize public variables
    public static volatile int counter = 1;
    public static volatile String temp = "";
    public static volatile String wind = "";
    public static volatile String place = "Mississauga";
    public static volatile int prevVal = 0;
    public static boolean newCity = false;
    public static ArrayList<Double> temperatureList = new ArrayList<>();


    public static void Arduino() throws IOException, InterruptedException
    {
    /* Setup arduino
    - Get COM port and store it in string variable
    - Create arduino object
    - Start and make sure arduino properly initializes
     */


        String port = "COM6";
        IODevice arduino = new FirmataDevice(port);
        arduino.start();
        arduino.ensureInitializationIsDone();

        //Pins
        Pin touchModule = arduino.getPin(7);
        Pin pushButton = arduino.getPin(6);
        Pin temperatureSensor = arduino.getPin(14);


        //I/O
        touchModule.setMode(Pin.Mode.INPUT);
        pushButton.setMode(Pin.Mode.INPUT);
        temperatureSensor.setMode(Pin.Mode.ANALOG);


        //Set up I2C display
        I2CDevice i2cObject = arduino.getI2CDevice((byte) 0x3C);
        SSD1306 display = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        display.init();




     //      ButtonListener obj = new ButtonListener(pushButton, temperatureSensor, display, touchModule);
        WeatherTask weatherTaskObj = new WeatherTask();

        temp = weatherTaskObj.Temperature();
        wind = weatherTaskObj.Wind();

        Timer timer = new Timer();
        Timer timer2 = new Timer();

        System.out.println(counter);
        //Event listener
        arduino.addEventListener(new ButtonListener(pushButton, temperatureSensor, display, touchModule));


        TimerTask temperatureTask = new TemperatureTask(temperatureSensor, display, timer);
        timer.schedule(temperatureTask, 0, 1000);

        TimerTask weatherTask = new eecs1021.WeatherTask();
        timer2.schedule(weatherTask,0,1000);

        //Setup for graph

        //Axis
        StdDraw.setXscale(-50,100);
        StdDraw.setYscale(-30,100);

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);

        //Draw Axes
        StdDraw.line(0,0,0,50);
        StdDraw.line(0,0,50,0);

        //Labels
        StdDraw.text(-30,25, "Local Temperature (°C)");
        StdDraw.text(25,-10, "Time");
        StdDraw.text(30,60,"Time vs Local Temperature");
        StdDraw.text(-5,0,"0");
        StdDraw.text(-5,50,"50");
        int i = 1;
        int j = 1;
        double temperature;

        while (true) {

            Thread.sleep(1100);

            temperature = temperatureList.get(j);
            StdDraw.setPenRadius(0.75);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text((double)i,temperature,"⋅");
            System.out.println("temp is " + temperature);

            j++;
            if(i < 50) {
                i++;
            }
            else{
                i = 1;
            }


        }
    }



}




