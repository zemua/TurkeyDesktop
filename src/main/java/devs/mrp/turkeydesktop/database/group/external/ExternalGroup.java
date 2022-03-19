/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import java.util.Objects;

/**
 *
 * @author miguel
 */
public class ExternalGroup {
    
    public static final String ID = "ID";
    public static final String GROUP = "GROUP";
    public static final String FILE = "FILE";
    
    private long id;
    private long group;
    private String file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroup() {
        return group;
    }

    public void setGroup(long group) {
        this.group = group;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + (int) (this.group ^ (this.group >>> 32));
        hash = 37 * hash + Objects.hashCode(this.file);
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
        final ExternalGroup other = (ExternalGroup) obj;
        if (this.group != other.group) {
            return false;
        }
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

}
