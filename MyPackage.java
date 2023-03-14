package etu1840.framework.util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
public class MyPackage{
    public MyPackage(){
    }
    public Vector<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
       Vector<Class<?>> classes=new Vector<Class<?>>();
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            x.printStackTrace();
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                File file = new File(directory.getAbsolutePath() + File.separator + files[i]);
                System.out.println( " path : "+file.getAbsolutePath() );
                if (file.isDirectory()) {
                    classes.addAll(this.getClasses(packageName + "." + file.getName()));
                } else {
                    if (files[i].endsWith(".class")) {
                        // removes the .class extension
                        System.out.println( "files : "+files[i] );
                        classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
                    }
                }
            }
        } else {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
        }
        return classes;
    }

    public void listAnnoted( Class clazz , Vector<Class<?>> l ){
        int i = 0;
        for( i = 0 ; i != l.size() ; i++ ){
            if( l.get(i).isAnnotationPresent( clazz ) ){
                System.out.println( " class : "+l.get(i).getSimpleName() );
            }
        }
    }
}