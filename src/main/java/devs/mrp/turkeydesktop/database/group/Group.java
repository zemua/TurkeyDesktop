/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

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
    
    private long id;
    private String name;
    private GroupType type;
    
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
    
    public boolean equals(Group group) {
        return group != null
                && ((this.getName() == null && group.getName() == null) || this.getName().equals(group.getName()))
                && ((this.getType() == null && group.getType() == null) || this.getType().equals(group.getType()));
    }
    
    @Override
    public String toString() {
        return this.name;
    }

}
