package devs.mrp.turkeydesktop.database.titles;

public class Title {
    
    public static final String SUB_STR = "SUB_STR";
    public static final String TYPE = "TYPE";
    
    private String subStr;
    private Title.Type type;
    
    public enum Type {
        POSITIVE, NEGATIVE, NEUTRAL;
    }

    public String getSubStr() {
        return subStr;
    }

    public void setSubStr(String subStr) {
        this.subStr = subStr;
    }

    public Type getType() {
        return type;
    }
    
    public String getTypeString() {
        return type.toString();
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        String sign = type.equals(Title.Type.POSITIVE) ? "+" : "-";
        return "[" + sign + "] " + subStr;
    }
    
    public static boolean isValidKey(String titleSubString) {
        return titleSubString != null && !titleSubString.isEmpty();
    }
    
}
