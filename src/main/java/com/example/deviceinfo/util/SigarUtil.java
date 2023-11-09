package com.example.deviceinfo.util;

import java.io.File;
import com.google.common.io.Resources;
import org.hyperic.sigar.Sigar;
public class SigarUtil {
    private static class SigarUtilHolder{
        private static final SigarUtil INSTANCE = new SigarUtil();
        private static final Sigar Sigar = new Sigar();
    }
    private SigarUtil (){
        try {
            String file = Resources.getResource("sigar/.sigar_shellrc").getFile();
            File classPath = new File(file).getParentFile();
            String path = System.getProperty("java.library.path");
            if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);
            System.out.println(path);
        } catch (Exception e) {
        }
    }
    public static final Sigar getInstance(){
        return SigarUtilHolder.Sigar;
    }
    public static final SigarUtil getSigarUtilInstance(){
        return SigarUtilHolder.INSTANCE;
    }
}