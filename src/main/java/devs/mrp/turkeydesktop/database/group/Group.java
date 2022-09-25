/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import java.util.Objects;

/**
 *
 * @author miguel
 */
public class Group {
    
    // TODO add group of app when loggin time for each entry
    
    public static final String GROUP = "APP_GROUP";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String CLOSEABLE = "CLOSEABLE";
    
    private long id;
    private String name;
    private GroupType type;
    private Boolean closeable;

    public boolean isCloseable() {
        if (GroupType.POSITIVE.equals(type)) {
            return false;
        }
        if (Objects.isNull(closeable)) {
            return true;
        }
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
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
