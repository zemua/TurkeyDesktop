package devs.mrp.turkeydesktop.database.titles;

import java.util.Objects;

public class Title {
    
    public static final String SUB_STR = "SUB_STR";
    public static final String TYPE = "TYPE";
    
    private String subStr;
    private Title.Type type;
    
    public Title(){
    }
    
    public Title(String substring, Title.Type t) {
        this.subStr = substring;
        this.type = t;
    }
    
    public static Title from(Title title) {
        Title result = new Title();
        result.setSubStr(title.getSubStr());
        result.setType(title.getType());
        return result;
    }
    
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.subStr);
        hash = 37 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Title other = (Title) obj;
        if (!Objects.equals(this.subStr, other.subStr)) {
            return false;
        }
        return this.type == other.type;
    }
    
    
    
}
