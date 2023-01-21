package devs.mrp.turkeydesktop.common.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static org.mockito.Mockito.mock;

public class CommonMocks {
    
    private static Map<String,Object> mocks = new HashMap<>();
    
    public static <T> T getMock(Class<T> clazz) {
        String name = clazz.getSimpleName();
        T result = (T)mocks.get(name);
        if (Objects.isNull(result)) {
            result = saveMock(name, clazz);
        }
        return result;
    }
    
    private static <T> T saveMock(String name, Class<T> clazz) {
        T result = mock(clazz);
        mocks.put(name, result);
        return result;
    }
    
}
