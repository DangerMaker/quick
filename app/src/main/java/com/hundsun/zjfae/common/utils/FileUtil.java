package com.hundsun.zjfae.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.hundsun.zjfae.common.base.BaseApplication;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import okhttp3.ResponseBody;

/**
 * file util
 *
 * @author ch
 */
public class FileUtil {

    private static final String iconName = "icon";//存放icon图文件夹

    private static final String attachment = "attachment";//存放附件

    public static final String TAKE = "take";//拍照路径

    public static final String PROOF = "proof";//合格投资资产证明

    public static final String UN_BIND_CARD = "unBandCard";//解绑卡

    public static final String CHANGE_CARD = "changeCard";//申请换卡


    public static final String WEB_KF = "web_kf";//web客服


    public static void init(Context context) {

        File iconFile = getDiskFileDir(context, iconName);

        if (!iconFile.exists()) {
            iconFile.mkdir();
        }

        File attachmentFile = getDiskFileDir(context, attachment);
        if (!attachmentFile.exists()) {
            attachmentFile.mkdir();
        }
    }


    public static File getIconFile(Context context, String fileName) {
        File iconFile = getDiskFileDir(context, iconName);
        File picFile = new File(iconFile, fileName + PNGSuffix);
        return picFile;
    }

    public static final String ATTACHMENT = "attachment";//附件文件夹


    public static final String TXTSuffix = ".txt";
    public static final String PDFSuffix = ".pdf";
    public static final String PNGSuffix = ".png";


    public static File saveFile(String filePath, ResponseBody body) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            if (filePath == null) {
                return null;
            }
            file = new File(filePath);
            if (file != null || !file.exists()) {
                file.createNewFile();
            }


            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            byte[] fileReader = new byte[1024];

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;

            }

            outputStream.flush();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }


    /**
     * 打开apk
     *
     * @param context
     * @param pathName 文件路径
     */
    public static void openFile(Context context, String pathName, String authority) {
        if (pathName == null) {
            return;
        }
        File file = new File(pathName);
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkUri = FileProvider.getUriForFile(context, authority, file);
            CCLog.e("apkUri", apkUri);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public static String filePath(Bitmap bitmap) {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutStream);
        BufferedOutputStream bos = null;
        FileOutputStream out = null;
        File filePic = null;
        byte[] bytes = byteOutStream.toByteArray();
        try {
            String fileName = UUID.randomUUID().toString();
            File iconFile = getDiskFileDir(BaseApplication.getInstance(), "icon");
            filePic = new File(iconFile, fileName + ".png");
            if (filePic.exists()) {
                filePic.delete();
            }
            filePic.createNewFile();
            out = new FileOutputStream(filePic);
            bos = new BufferedOutputStream(out);
            bos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }

            bitmap.recycle();
            bitmap = null;
        }
        return filePic.getPath();
    }


    public static String getFileName() {

        String fileName = UUID.randomUUID().toString();

        return fileName;
    }

    /*
     * 保存数据
     * */
    public static boolean saveUpdateFile(byte[] bytes, File filePath) {
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        if (filePath.exists()) {
            filePath.delete();
        }
        try {
            filePath.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(filePath);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭创建的流对象
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        return true;
    }

    public static String getFileDate(File filePath) {
        String content = "";
        File file = new File(filePath.getPath());
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                String date = EncDecUtil.AESDecrypt(new String(bos.toByteArray(), "UTF-8"));
                JSONObject os = new JSONObject(date);
                content = os.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }


    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }





    public static File getDiskFileDir(Context context, String fileName) {
        String path = "";

        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                if (context.getExternalFilesDir(fileName) != null){
                    path = context.getExternalFilesDir(fileName).getPath();
                }
            } else {
                if (context.getFilesDir() != null){
                    path = context.getFilesDir().getPath();
                }

            }
        }


        return new File(path);
    }



    public static String getFilePath(Context context,String fileName){
        File filePath = getDiskFileDir(context, fileName);
        if (!filePath.exists()) {

            filePath.mkdir();
        }

        return filePath.getPath();
    }



    public static String getTakePath(Context context) {

        File filePath = getDiskFileDir(context, TAKE);

        if (!filePath.exists()) {

            filePath.mkdir();
        }

        return filePath.getPath();
    }




    public static String getProof(Context context) {

        File filePath = getDiskFileDir(context, PROOF);

        if (!filePath.exists()) {

            filePath.mkdir();
        }

        return filePath.getPath();
    }


    public static String getUnBindCard(Context context) {

        File filePath = getDiskFileDir(context, UN_BIND_CARD);

        if (!filePath.exists()) {

            filePath.mkdir();
        }

        return filePath.getPath();
    }

    public static String getChangeCard(Context context) {
        File filePath = getDiskFileDir(context, CHANGE_CARD);

        if (!filePath.exists()) {

            filePath.mkdir();
        }

        return filePath.getPath();
    }


    /**
     * @param folderName 文件夹名字
     * @param fileName   文件名字
     **/
    public static File createFile(Context context, String folderName, String fileName) {

        File filePath = getDiskFileDir(context, folderName);

        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File file = new File(filePath, fileName);

        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static File createFile(Context context,String fileName){
        File file = getDiskFileDir(context,null);
        File filePath = new File(file,fileName);
        if (!filePath.exists()){
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }



    /**
     * @param folderName 文件夹名字
     * @param fileName   文件名字
     **/
    public static File getFile(Context context, String folderName, String fileName) {

        File filePath = getDiskFileDir(context, folderName);

        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File file = new File(filePath, fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    public static void deleteFile(File file) {

        if (file != null && file.exists()) {
            file.delete();
        }
    }


    public static void deleteFile(String path) {

        if (path != null && path != "" && !path.equals("")) {

            File file = new File(path);

            if (file.exists()) {

                File[] files = file.listFiles();

                if (files.length != 0) {

                    for (File f : files) {
                        if (f.isFile()) {

                            if (f.exists()) {

                                f.delete();
                            }

                        }
                    }
                }


                file.delete();
            }

        }
    }



}
