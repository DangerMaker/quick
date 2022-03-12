package com.hundsun.zjfae.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2016/9/1.
 */
public class PictureUtil {
    /**
     * 根据路径得到图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap ratio(String path, float pixelW, float pixelH) {
        Bitmap image = getBitmap(path);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 512) {//判断如果图片大于0.5M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        pixelH = image.getHeight() / 2;
        pixelW = image.getWidth() / 2;
        ByteArrayInputStream is = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = null;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        image.recycle();
        return bitmap;
    }


    public static Bitmap getSmallBitmap(long size, String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;

    }

    /**
     *
     * 检查bitmap图片是否大于2M
     * @param bmp
     * @return
     */
    public static boolean checkSizeMore2M(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        Log.d("test before", baos.toByteArray().length / 1024 + "");
        if (baos.toByteArray().length / 1024 >= 1024 * 2) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static long getAutoFileOrFilesSize(String filePath) {
        long blockSize = 0;
        if (StringUtils.isNotBlank(filePath)) {
            File file = new File(filePath);
            try {
                blockSize = getFileSize(file);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("获取文件大小", "获取失败!");
            }
        }
        return blockSize;
    }


    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * Bitmap转换为String
     *
     * @param bitmap
     * @return
     */
    public static String getString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    public static String compressBmpToFile(Bitmap bmp, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        Log.d("test before", baos.toByteArray().length / 1024 + "");
        if (baos.toByteArray().length / 1024 > maxSize) {
            bmp = zoomImage(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2);
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        while (baos.toByteArray().length / 1024 > maxSize) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        byte[] b = baos.toByteArray();
        Log.d("test after", b.length / 1024 + "");
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static Bitmap compressBmpToBmp(Bitmap bmp, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        Log.d("test before", baos.toByteArray().length / 1024 + "");
        if (baos.toByteArray().length / 1024 > maxSize) {
            bmp = zoomImage(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2);
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        while (baos.toByteArray().length / 1024 > maxSize) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        byte[] b = baos.toByteArray();
        Log.d("test after", b.length / 1024 + "");
        return bmp;

    }

    public static Bitmap compressBmpToBmp(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        Log.d("test before", baos.toByteArray().length / 1024 + "");
        if (baos.toByteArray().length / 1024 > 40) {
            bmp = zoomImage(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2);
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        while (baos.toByteArray().length / 1024 > 40) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        byte[] b = baos.toByteArray();
        Log.d("test after", b.length / 1024 + "");
        return bmp;

    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


//    public static int getBitmapSize(Bitmap bmp) {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
////            return bitmap.getAllocationByteCount();
////        }
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
////            return bitmap.getByteCount();
////        }
////        return bitmap.getRowBytes() * bitmap.getHeight();                 //earlier version
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int options = 80;//个人喜欢从80开始,
//        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        Log.d("test before", baos.toByteArray().length / 1024 + "");
//        if (baos.toByteArray().length / 1024 > 40) {
//            bmp = zoomImage(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2);
//            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        }
//        return baos.toByteArray().length / 1024;
//    }

//    public static boolean checkSize(String filePath) {
//        try {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(filePath, options);
//            options.inJustDecodeBounds = false;
//            return checkSize(BitmapFactory.decodeFile(filePath, options));
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
