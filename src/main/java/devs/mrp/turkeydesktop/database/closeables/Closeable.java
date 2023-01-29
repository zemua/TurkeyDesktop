package devs.mrp.turkeydesktop.database.closeables;

import java.util.Objects;

public class Closeable {
    
    public static final String PROCESS_NAME = "PROCESS_NAME";

    public Closeable(String process) {
        this.process = process;
    }

    public Closeable() {
    }
    
    private String process;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.process);
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
        final Closeable other = (Closeable) obj;
        return Objects.equals(this.process, other.process);
    }
    
}
