package eecs1021;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class WeatherTaskTest {

    @Test
    void URLTest() {

        String location = "Toronto";
        Map<String, Object> respMap = null;

        respMap = WeatherTask.URL(location, respMap);

        assertNotNull(respMap);
        assertTrue(respMap.containsKey("cod"));

        assertEquals("200", String.valueOf(((Double) respMap.get("cod")).intValue()));
    }

    @Test
    void testTemperature() {

        String temperature = WeatherTask.Temperature();
        assertNotNull(temperature);
        assertFalse(temperature.isEmpty());
        assertTrue(Double.parseDouble(temperature) >= -100);
        assertTrue(Double.parseDouble(temperature) <= 100);
    }

    @Test
    void testWind() {

        String windSpeed = WeatherTask.Wind();


        assertNotNull(windSpeed);
        assertFalse(windSpeed.isEmpty());
        assertTrue(Double.parseDouble(windSpeed) >= 0);
        assertTrue(Double.parseDouble(windSpeed) <= 100);
    }

}
