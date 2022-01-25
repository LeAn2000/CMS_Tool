package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ChildMonitoringSystem.CMS.Model.INFO_APP;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.ChildMonitoringSystem.CMS.Value.Constant.myDir;

public class LoadInfoAppFromDevice {



    public ArrayList<INFO_APP> getAllAppInstalled(Context ctx) {
        List<PackageInfo> apps = ctx.getPackageManager().getInstalledPackages(0);
        ArrayList<INFO_APP> res = new ArrayList<>();
        new Thread(() -> {
            for (int i = 0; i < apps.size(); i++) {
                PackageInfo p = apps.get(i);
                if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    PackageItemInfo a = new PackageItemInfo();
                    INFO_APP newInfo = new INFO_APP();
                    newInfo.SERI_PHONE = Constant.Serial(ctx);
                    newInfo.APPNAME = p.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
                    newInfo.pNAME = p.packageName;
                    newInfo.verNAME = p.versionName;
                    newInfo.verCODE = p.versionCode;
                    Bitmap bit = getBitmapFromDrawable(p.applicationInfo.loadIcon(ctx.getPackageManager()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        try {
                            newInfo.icon = saveImage(bit, p.applicationInfo.loadLabel(ctx.getPackageManager()).toString(), ctx);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        newInfo.icon = SaveImage(p.applicationInfo.loadLabel(ctx.getPackageManager()).toString(), bit);
                    }
                    res.add(newInfo);
                } else
                    continue;

            }
        }).start();
        return res;
    }

    @NonNull
    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private String saveImage(Bitmap bitmap, @NonNull String name, Context mContext) throws IOException {
        OutputStream fos;
        String filename = name + ".png";
        String subfolder = "/Appinstallindevice";
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + subfolder);
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        fos = resolver.openOutputStream(imageUri);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + subfolder + "/" + filename;
    }

    private String SaveImage(String appname, Bitmap bit) {
        myDir.mkdirs();
        String fname = appname + ".jpg";
        File file = new File(myDir, fname);
        Log.i("TAG", "" + file.getPath());
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bit.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static void deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
            file.delete();
        }
    }

}
