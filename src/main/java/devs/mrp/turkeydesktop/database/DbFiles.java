/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.io.File;

/**
 *
 * @author miguel
 */
public class DbFiles {
    static String rootpath;
    private static final String FOLDER_NAME = "TurkeyDesktop";
    
    public static String getDbPath(){
        String path = ArchivosModel.getResetedPath().concat(File.separator + "db");
        File f = new File(path);
        if (!f.isDirectory()){
            f.mkdir();
        }
        return path;
    }
    
    public static String getDbFilePath(){
        return getDbPath().concat(File.separator + FOLDER_NAME);
    }
}
