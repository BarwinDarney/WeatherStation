package eecs1021;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.firmata4j.ssd1306.SSD1306;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class WeatherTask extends TimerTask{
    private String s = "";
    private Map<String, Object> map = toMap("");
    @Override
    public void run(){


        if(mainObj.newCity == true){
            System.out.println("API Weather");

            URL(s, map);
            Temperature();
            Wind();
            mainObj.newCity = false;
        }
        else {
            return;
        }

    }

    private boolean newCity = mainObj.newCity;
    static eecs1021.Main mainObj = new eecs1021.Main();


    public static Map<String, Object> toMap(String string){
        Map<String, Object> map = new Gson().fromJson(
                string, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
        return map;
    }


    public static Map<String, Object> URL(String str, Map<String, Object> respMap) {
        //  String location = Location(str);
        //  place = location;
        String place = mainObj.place;
        String location = place;
        //    System.out.println("Place is " + location);
        String API_Key = "e5484c57d9ed0f6619699f919146174f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_Key + "&units=metric";

        try {
            //Create StringBuilder, URL, URLConnection, and BufferedReader objects
            StringBuilder strBuilderObj = new StringBuilder();
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            BufferedReader readerObj = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //Create a string variable
            String string;
            //Have a while loop to read through data
            while ((string = readerObj.readLine()) != null) {
                strBuilderObj.append(string);
            }
            readerObj.close();
            System.out.println(strBuilderObj);

            respMap = toMap(strBuilderObj.toString());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return  respMap;

    }
    public static Map<String, Object> MainMap(Map<String, Object> mainMap, Map<String, Object> map) {
        String str = "";
        Map respMap = URL(str, map);
        mainMap = toMap(respMap.get("main").toString());

        return mainMap;
    }

    public static Map<String, Object> WindMap(Map<String, Object> windMap, Map<String, Object> map){
        String s = "";
        Map respMap = URL(s, map);
        windMap = toMap(respMap.get("wind").toString());

        return windMap;
    }


    public static String Temperature() {
        String temp = "";
        //     System.out.println("Global temp is " + mainObj.temp);
        //   System.out.println("Prev temp" + temp);
        Map<String, Object> map = toMap("");
        Map<String, Object> mapTwo = toMap("");
        Map<String, Object> mainMap = MainMap(map,mapTwo);
        temp = mainMap.get("temp").toString();
        mainObj.temp = temp;
        //     System.out.println("Temp is " + temp);
        //     System.out.println("Global temp is " + mainObj.temp);

        return temp;
    }

    public static String Wind(){
        String wind = "";
        Map<String, Object> map = toMap("");
        Map<String, Object> mapTwo = toMap("");
        Map<String, Object> windMap = WindMap(map,mapTwo);
        wind = windMap.get("speed").toString();

        mainObj.wind = wind;

        return wind;


    }



}




