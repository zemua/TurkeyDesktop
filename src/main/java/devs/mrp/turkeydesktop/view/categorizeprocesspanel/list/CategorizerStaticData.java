/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author miguel
 */
public class CategorizerStaticData {
    
    private static Map<String, Type.Types> types;
    private static List<FeedbackListener<Type.Types,String>> listeners = new ArrayList<>();
    
    public static void setTypes(Map<String, Type.Types> types) {
        CategorizerStaticData.types = types;
    }
    
    public static Map<String, Type.Types> getTypes() {
        if (types == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(types);
    }
    
    public static Type.Types getTypeOf(String process) {
        if (types == null || !types.containsKey(process)) {
            return Type.Types.UNDEFINED;
        }
        return types.get(process);
    }
    
    public static void addListener(FeedbackListener<Type.Types,String> listener) {
        listeners.add(listener);
    }
    
    public static void removeListener(FeedbackListener<Type.Types,String> listener) {
        listeners.remove(listener);
    }
    
    public static boolean hasListener(FeedbackListener<Type.Types,String> listener) {
        return listeners.contains(listener);
    }
    
    public static void giveFeedback(Type.Types type, String processName) {
        listeners.forEach(l -> l.giveFeedback(type, processName));
    }
    
}
