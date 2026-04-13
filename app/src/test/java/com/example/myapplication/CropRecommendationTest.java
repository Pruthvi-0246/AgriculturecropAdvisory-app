package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class CropRecommendationTest {

    @Test
    public void testCropLogic() {
        // Simple logic test for recommendation
        String season = "Kharif (Monsoon)";
        String soil = "Alluvial";
        
        String result = getMockRecommendation(season, soil);
        assertTrue(result.contains("Rice"));
    }

    private String getMockRecommendation(String season, String soil) {
        if (season.contains("Kharif")) {
            if (soil.equals("Alluvial") || soil.equals("Black")) {
                return "Rice, Cotton, or Maize";
            }
        }
        return "Unknown";
    }
}