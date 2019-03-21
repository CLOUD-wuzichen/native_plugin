package com.kwl.native_plugin.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.kwl.native_plugin.BuildConfig;
import com.kwl.native_plugin.utils.URIUtils;

import java.io.File;

import androidx.core.content.FileProvider;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

/**
 * Created by wu.zc on 2019/3/20
 * e-mail : wuzc@szkingdom.com
 * desc :
 */
public class ImagePickDelegate implements PluginRegistry.ActivityResultListener,
        PluginRegistry.RequestPermissionsResultListener {

    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;  //使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;  //使用相册中的图片
    public static final int REQUEST_CROP_PHOTO = 3;  //截图

    public static final String CODE_SD_CARD_NO_EXIT = "-1";  //SD卡不存在
    public static final String CODE_NO_CAMERA_PERMISSION = "-2";  //"获取相机权限失败，请前往设置进行修改";

    private final Activity activity;
    private final String externalFilesDirectory;

    private String imagePath;   //拍照路径
    private Uri uriTempFile;   //裁剪后保存的uri
    private MethodChannel.Result result;

    public ImagePickDelegate(Activity activity, String externalFilesDirectory) {
        this.activity = activity;
        this.externalFilesDirectory = externalFilesDirectory;
    }

    public void pickPhoto(MethodChannel.Result result) {
        this.result = result;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    public void takePhoto(MethodChannel.Result result) {
        this.result = result;
        //判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imagePath = externalFilesDirectory + "_take.jpg";
            // 根据文件地址创建文件
            File file = new File(imagePath);
            if (file.exists()) {
                if (!file.delete()) {

                }
            }
            // 把文件地址转换成Uri格式
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            result.error(CODE_SD_CARD_NO_EXIT + "", "SD卡不存在", "");
        }
    }


    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        //裁剪后保存到本地，防止部分机型无法return-data
        uriTempFile = Uri.parse("file://" + "/" + externalFilesDirectory + "_crop.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        activity.startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            //选择图库
            if (data == null || data.getData() == null) {
                return true;
            }
            cropPhoto(data.getData());
        } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            // 相机直接拍照
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(activity, activity.getPackageName()
                        + ".fileprovider", new File(imagePath));
            } else {
                uri = Uri.fromFile(new File(imagePath));
            }
            cropPhoto(uri);
        } else if (requestCode == REQUEST_CROP_PHOTO) {
            //裁剪完成
            String path = URIUtils.getImageAbsolutePath(activity, uriTempFile);
            result.success(path);
            result = null;
        }
        return true;
    }

    @Override
    public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
        return false;
    }
}
