package devs.mrp.turkeydesktop.database.group.expor;

import java.util.Objects;

public class ExportedGroup {
    
    public static final int MAX_PATH_SIZE = 500;
    public static final String GROUP = "GROUP_COLUMN";
    public static final String FILE = "FILE";
    public static final String DAYS = "FOR_DAYS";
    
    private long group;
    private String file;
    private long days;

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

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.group ^ (this.group >>> 32));
        hash = 53 * hash + Objects.hashCode(this.file);
        hash = 53 * hash + (int) (this.days ^ (this.days >>> 32));
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
        final ExportedGroup other = (ExportedGroup) obj;
        if (this.group != other.group) {
            return false;
        }
        if (this.days != other.days) {
            return false;
        }
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }
    
}
