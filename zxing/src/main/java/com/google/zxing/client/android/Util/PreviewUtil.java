package com.google.zxing.client.android.Util;

import android.graphics.Point;

/**
 * Author: duke
 * DateTime: 2021-09-25 23-32
 * Description: 功能说明
 */
public class PreviewUtil {

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
    public static Point repairVerticalPreviewStretch(Point point) {
        if (point == null) {
            return null;
        }
        int a = point.x;
        int b = point.y;
        if (a < b) {
            // 交换大小
            a = a + b;
            b = a - b;
            a = a - b;
        }
        return new Point(a, b);
    }

}
