package devs.mrp.turkeydesktop.database.group;

import java.util.Objects;

public class Group {
    
    public static final int ID_COLUMN = 1;
    public static final String GROUP = "APP_GROUP";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String PREVENT_CLOSE = "PREVENT_CLOSE";
    public static final String DISABLE_POINTS = "DISABLE_POINTS";
    
    private long id;
    private String name;
    private GroupType type;
    private Boolean preventClose;
    private Boolean disablePoints;

    public boolean isPreventClose() {
        if (GroupType.POSITIVE.equals(type)) {
            return true;
        }
        if (Objects.isNull(preventClose)) {
            return false;
        }
        return preventClose;
    }
    
    public boolean isDisablePoints() {
        if (GroupType.NEGATIVE.equals(type)) {
            return true;
        }
        if (Objects.isNull(disablePoints)) {
            return false;
        }
        return disablePoints;
    }

    public void setPreventClose(boolean closeable) {
        this.preventClose = closeable;
    }
    
    public void setDisablePoints(boolean disabled) {
        this.disablePoints = disabled;
    }
    
    public enum GroupType {
        POSITIVE, NEGATIVE;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }
    
        @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.type);
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
        final Group other = (Group) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

}
