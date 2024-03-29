package com.google.zxing.client.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;

import com.google.zxing.PlanarYUVLuminanceSource;

/**
 * Author: duke
 * DateTime: 2021-09-25 23-32
 * Description: 功能说明
 */
public final class PreviewUtil {

    public static boolean isOrientationPortrait(Context context) {
        if (context == null
                || context.getResources() == null
                || context.getResources().getConfiguration() == null) {
            // 默认竖屏扫码
            return true;
        }
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 交换 x 和 y 的值，解决竖屏扫码预览时图像拉伸
     * <p>
     * 修改代码 CameraConfigurationManager.initFromCameraParameters(OpenCamera camera):
     * // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     * // 交换大小，修复预览图像拉伸
     * Point newPoint = repairVerticalPreviewStretch(screenResolution);
     * <p>
     * // cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
     * cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, newPoint);
     * <p>
     * // bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
     * bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, newPoint);
     * <p>
     * // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     *
     * @param point 原 point 对象
     * @return 无返回值，直接在原 point 对象内存地址里面修改
     */
    public static Point repairVerticalPreviewStretch(Context context, Point point) {
        if (point == null) {
            return null;
        }
        int a = point.x;
        int b = point.y;
        if (isOrientationPortrait(context)) {
            // 竖屏扫码模式，交换宽高大小
            a = a + b;
            b = a - b;
            a = a - b;
        }
        return new Point(a, b);
    }

    /**
     * (设置竖屏后)，解决 [近距离无法识别二维码] 问题
     * <p>
     * 修改 CameraManager.buildLuminanceSource(...) 方法
     * return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
     */
    public static PlanarYUVLuminanceSource getNewPlanarYUVLuminanceSource(byte[] data, int width, int height) {
        // Go ahead and assume it's YUV rather than die.
//        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
//                rect.width(), rect.height(), false);
        return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
    }

    /**
     * 修正预览框宽高
     */
    public static Rect getFramingRectInPreview(Context context,
                                               Rect rect,
                                               Point cameraResolution,
                                               Point screenResolution) {
        if (isOrientationPortrait(context)) {
            // 竖屏
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
        } else {
            // 横屏
            rect.left = rect.left * cameraResolution.x / screenResolution.x;
            rect.right = rect.right * cameraResolution.x / screenResolution.x;
            rect.top = rect.top * cameraResolution.y / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
        }
        return rect;
    }

    /**
     * 解决 PDF_417/DATA_MATRIX 格式乱码问题
     */
    public static String reDecodeText(String oldText) {
        String newStr = oldText;
        if (TextUtils.isEmpty(newStr)) {
            return newStr;
        }

        try {
            String CHARSET_ISO_8859_1 = "ISO-8859-1";
            String CHARSET_UTF_8 = "UTF-8";

            // boolean canEncode = Charset.forName(oldCharSet).newEncoder().canEncode(newStr);
            // CharsetDecoder decoder = Charset.forName(newStr).newDecoder();

            if (newStr.equals(new String(newStr.getBytes(CHARSET_ISO_8859_1), CHARSET_ISO_8859_1))) {
                newStr = new String(newStr.getBytes(CHARSET_ISO_8859_1), CHARSET_UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * 绘制中间的 Logo
     *
     * @param srcBitmap  二维码位图
     * @param logoBitmap logo 位图
     */
    public static void drawLogo(Bitmap srcBitmap, Bitmap logoBitmap) {
        if (srcBitmap == null || logoBitmap == null) {
            // 如果二维码 Bitmap 为 null，或者不需要设置 logo，返回二维码 Bitmap。
            return;
        }

        //获取图片的宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        // 调整 logoBitmap 大小为二维码 bitmap 的 1/5
        logoBitmap = scaleBitmap(logoBitmap, srcWidth, srcHeight);

        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        Canvas srcCanvas = new Canvas(srcBitmap);

        // 在二维码 bitmap 中心绘制 logo
        srcCanvas.drawBitmap(logoBitmap, (srcWidth - logoWidth) >> 1, (srcHeight - logoHeight) >> 1, null);
    }

    /**
     * 缩放 Bigmap
     *
     * @param origin       原始 bitmap
     * @param targetWidth  目标 width
     * @param targetHeight 目标 height
     * @return newBitmap
     */
    private static Bitmap scaleBitmap(Bitmap origin, int targetWidth, int targetHeight) {
        if (origin == null || targetWidth <= 0 || targetHeight <= 0) {
            return origin;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();

        // 计算 logo 的缩放比例。logo 的大小为二维码总体大小的 1/n。
        int n = 4;
        float scaleFactorWidth = targetWidth * 1.0f / n / width;
        float scaleFactorHeight = targetHeight * 1.0f / n / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactorWidth, scaleFactorHeight);
        Bitmap newLogoBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);

        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newLogoBitmap;
    }
}
