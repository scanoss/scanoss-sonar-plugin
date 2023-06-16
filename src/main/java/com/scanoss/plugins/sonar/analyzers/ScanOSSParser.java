package com.scanoss.plugins.sonar.analyzers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scanoss.plugins.sonar.model.ScanData;
import org.sonar.api.utils.log.Loggers;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ScanOSSParser {

    private static final org.sonar.api.utils.log.Logger LOGGER = Loggers.get(ScanOSSParser.class);

    public static Map<String,List<ScanData>> parseScanResult(String result){

        Gson gson = new Gson();
        Type listType = new TypeToken<Map<String,List<ScanData>>>() {}.getType();
        Map<String,List<ScanData>> originalObject = gson.fromJson(result, listType);

        return originalObject;
    }
}
