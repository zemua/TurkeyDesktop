/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author miguel
 */
public class CategorizerStaticData {
    
    private static HashMap<String, Type.Types> types;
    private static List<FeedbackListener<Type.Types,String>> listeners = new ArrayList<>();
    
    public static void setTypes(HashMap<String, Type.Types> types) {
        CategorizerStaticData.types = types;
    }
    
    public static void addListener(FeedbackListener<Type.Types,String> listener) {
        listeners.add(listener);
    }
    
    public static void removeListener(FeedbackListener<Type.Types,String> listener) {
        listeners.remove(listener);
    }
    
    public static void giveFeedback(Type.Types type, String processName) {
        listeners.forEach(l -> l.giveFeedback(type, processName));
    }
    
}
