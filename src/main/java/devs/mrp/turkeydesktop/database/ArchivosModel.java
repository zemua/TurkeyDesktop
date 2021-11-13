/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author miguel
 */
public class ArchivosModel {
    
    private static final String FOLDER_NAME = "TurkeyDesktop";
    
    String rootPath;
    URL url;
    BufferedImage img = null;
    private Map<String, ImageIcon> imageMap;
    private Integer imgWidth = null;
    public static int P_WIDTH = 51;

    public ArchivosModel() {
        resetRootPath();
    }

    /*
    Funciones para Modificar los ArrayList
     */
    public void mueveList(int drag, int drop) {
        // nada aquí por ahora
    }

    public void anadeList(int index, Path path, BufferedImage buffer) {
        // nada aquí por ahora
        anadeArchivo(path, buffer);
    }

    public static boolean anadeArchivo(Path path, BufferedImage buffer) {
        File outputfile = path.toFile();
        try {
            ImageIO.write(buffer, "png", outputfile);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ArchivosModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void borraList(Path path) {
        // nada aquí por ahora
        borraArchivo(path);
    }

    public void borraArchivo(Path path) {
        File f = path.toFile();
        f.delete();
    }

    public URI idToUri(int n) {
        URI uri = URI.create(rootPath.concat("/" + n + ".png"));
        return uri;
    }

    /*
    establecer la ruta dónde estarán las imágenes
    se ofrecen funciones adicionales para utilzar
    en los tests con una ruta diferente
     */
    public void setRootPath(String path) {
        rootPath = path;
    }

    public String getRootPath() {
        return rootPath;
    }

    public static String getResetedPath() {
        return System.getProperty("user.home").concat(File.separator + FOLDER_NAME);
    }

    public final String resetRootPath() {
        //rootPath = System.getProperty("user.home");
        //rootPath = rootPath.concat(File.separator + FOLDER_NAME);
        rootPath = getResetedPath();
        File f = new File(rootPath);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdir();
        }
        return rootPath;
    }

    public String setUrl(String mUrl) {
        try {
            this.url = new URL(rootPath.concat(mUrl));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ArchivosModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.url.getPath();
    }

    public URL getUrl() {
        return this.url;
    }

    /**
     * llamado desde PictoController
     *
     * @param i
     */
    public void setWidth(int i) {
        imgWidth = i;
        P_WIDTH = i;
    }

    public int getWidth() {
        if (isNull(imgWidth)) {
            return 0;
        }
        return imgWidth;
    }

    /*
    Leer archivo a una variable
    y también imagen
     */
    public File getFileFromUrl(String s) {
        setUrl(s);
        File file = null;
        file = new File(url.getPath());
        return file;
    }

    public BufferedImage leerImagen(File f) {
        try {
            if (isImage(Files.probeContentType(f.toPath()))) {
                try {
                    img = ImageIO.read(f);
                } catch (IOException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Tratando de buffear archivo que no es imagen! " + img);
                img = null;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivosModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }

    /*
    Hacer un listado de los archivos de imagen
    y luego pasarlos a un String[]
    para poder leerlos desde el MAP
    que los pasará al icono del listado Swing
     */
    public final ArrayList<BufferedImage> getBufferedImages(Path[] p) {
        ArrayList<BufferedImage> mListaBufferedImages = new ArrayList<>();
        for (Path p1 : p) {
            try {
                if (isImage(Files.probeContentType(p1))) {
                    try (final InputStream is = Files.newInputStream(p1)) {
                        BufferedImage bi = ImageIO.read(is);
                        mListaBufferedImages.add(bi);
                        is.close();
                    }
                }
            }catch (IOException ex) {
                Logger.getLogger(ArchivosModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return mListaBufferedImages;
    }

    // #A3
    public static Path[] getPathOfAllFiles(String dirPath) {
        File folder = new File(dirPath);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        String[] sList = folder.list();
        Path[] pList = new Path[sList.length];
        for (int i = 0; i < pList.length; i++) {
            File f = new File(dirPath + File.separator + sList[i]);
            if (f.exists()) {
                pList[i] = Paths.get(f.getPath());
            }
        }
        return pList;
    }

    private Path[] getPathOfAllFiles() {
        return getPathOfAllFiles(rootPath);
    }

    // #A2
    public static ArrayList<Path> getPathOfImagesOnly(String dirPath) {
        Path[] paths = getPathOfAllFiles(dirPath);
        ArrayList<Path> iPaths = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (checkIfImage(paths[i])) {
                iPaths.add(paths[i]);
            }
        }
        return iPaths;
    }

    public ArrayList<Path> getPathOfImagesOnly() {
        return getPathOfImagesOnly(rootPath);
    }

    public static boolean checkIfImage(Path p) {
        try {
            return isImage(Files.probeContentType(p));
        } catch (IOException ex) {
            Logger.getLogger(ArchivosModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean isImage(String mime) {
        if ("image/png".equals(mime) || "image/gif".equals(mime) || "image/jpg".equals(mime) || "image/jpeg".equals(mime) || "image/bmp".equals(mime)) {
            return true;
        }
        System.out.println("mime no es imagen: " + mime);
        return false;
    }

    /*
    * Crear mapa String-Imagen
    * Para usarlo en el render
    * en el listado del swing
     */
    private Map<String, ImageIcon> createImageMap(BufferedImage[] list) {
        Map<String, ImageIcon> map = new HashMap<>();
        for (Integer i = 0; i < list.length; i++) {
            map.put(i.toString(), new ImageIcon(list[i]));
        }
        return map;
    }

    public Map<String, ImageIcon> assignImageMap(BufferedImage[] namelist) {
        imageMap = createImageMap(namelist);
        return imageMap;
    }

    // #A1
    public Map<String, ImageIcon> getImagePathMap() {
        if (getWidth() < 1) {
            setWidth(P_WIDTH);
        }
        Map<String, ImageIcon> pathMap = new HashMap<>();
        ArrayList<Path> pathList = getPathOfImagesOnly();
        for (int i = 0; i < pathList.size(); i++) {
            try {
                Path p = Paths.get(pathList.get(i).toString());
                String name = p.getFileName().toString();
                if (name.contains(".")) {
                    name = name.substring(0, name.indexOf("."));
                }
                InputStream is = Files.newInputStream(p);
                BufferedImage bi = ImageIO.read(is);
                ImageIcon ii = new ImageIcon(bi);
                ii = scaleImage(ii, this.getWidth());
                pathMap.put(name, ii);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathMap;
    }

    /*
    Resize to fit into the right side
     */
    private Map<String, ImageIcon> resizeIcon(Map<String, ImageIcon> mMap) {
        Map<String, ImageIcon> smallMap = mMap;
        Set<String> k = mMap.keySet();
        String[] mKeys = k.toArray(new String[k.size()]);

        for (int i = 0; i < mMap.size(); i++) {
            ImageIcon mImg = mMap.get(mKeys[i]);
            mImg = scaleImage(mImg, imgWidth);
            smallMap.put(mKeys[i], mImg);
        }
        return smallMap;
    }

    /*
    Funciones para calcular redimensionamiento necesario para las imágenes del mapa
     */
    public static ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public static ImageIcon scaleImage(ImageIcon icon, int w) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
}
