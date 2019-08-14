package com.targeteducare;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;

public class StructureClass {
    private static Context context;
    private static String ST_EXT = "EXTERNAL_STORAGE";
    private static String ST_SEC = "SECONDARY_STORAGE";
    private static String ST_PRI = "PRIMARY_STORAGE";

    public static void defineContext(Context _context) {
        context = _context;
    }

    public static boolean isSecondaryStorageConnected(boolean exactCheck) {
        if (System.getenv(ST_EXT) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSecondaryStorageConnected() {
        boolean connected = false;
        if (System.getenv(ST_SEC) != null) {
            connected = true;
        } else {
            if (System.getenv(ST_EXT) != null) {
                connected = true;
            } else {
                if (System.getenv(ST_PRI) != null) {
                    connected = true;
                }
            }
        }
        return connected;
    }

    public static String getSecondaryStorage(boolean isExact) {
        //SECONDARY_STORAGE
        return System.getenv(ST_SEC);
    }

    public static String getSecondaryStorage() {
        //SECONDARY_STORAGE
        String path = null;
        if (System.getenv(ST_SEC) != null) {
            path = System.getenv(ST_SEC);
        } else {
            if (System.getenv(ST_EXT) != null) {
                path = System.getenv(ST_EXT);
            } else {
                path = System.getenv(ST_PRI);
            }
        }
        return path;
    }

    public static boolean isExternalStorageConnected() {
        if (System.getenv("EXTERNAL_STORAGE") != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPath(String path) {
        return path + "/";
    }

    public static String storagePath() {
        if (isSecondaryStorageConnected()) {
            return getSecondaryStorage();
        } else {
            return "";
        }
    }

    public static String generate(String name) {
        //String fold = "."+name;
        String fold = name;
        //String fold = context.getResources().getString(R.string.storage_name);
        //String root = storagePath();
        //String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String  root=Environment.getExternalStorageDirectory().getAbsolutePath();

        File f1 = new File(root);
        if (!f1.exists()) {
            f1.mkdir();
        }

        File f2=new File(root,".TargetEducare");
        if (!f2.exists()) {
            f2.mkdir();
        }

        File f = new File(f2.getAbsolutePath(), fold);
        if (!f.exists()) {
            f.mkdir();
        }
        Log.e("StructureClass", "" + f.getAbsolutePath());
        return getPath(f.getAbsolutePath());
    }

    public static void clearAll() {
        try {
            File f = new File(generate(context.getResources().getString(R.string.storage_name)));
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
