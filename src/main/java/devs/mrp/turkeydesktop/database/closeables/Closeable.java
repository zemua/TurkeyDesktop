/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

/**
 *
 * @author miguel
 */
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
    
}
