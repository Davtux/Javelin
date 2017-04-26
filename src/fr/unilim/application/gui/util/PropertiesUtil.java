package fr.unilim.application.gui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Util method for get and set properties.
 */
public class PropertiesUtil {

    public static void setPropertyStringList(Properties properties, String key, List<String> value){
        String str = value.toString();
        str = str.replace("]", "");
        str = str.replace("[", "");
        properties.setProperty(key, str);
    }

    public static List<String> getPropertyStringList(Properties properties, String key){
        List<String> list = new ArrayList<>();
        String str = properties.getProperty(key, null);
        if(str != null && !str.isEmpty()){
            str = str.replace(", ", ",");
            list.addAll(Arrays.asList(str.split(",")));
        }
        return list;
    }
}
