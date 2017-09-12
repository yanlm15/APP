package com.bignewsmaker.makebignews.extra_class;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by liminyan on 12/09/2017.
 * 提供了清除图片缓存的接口
 *
 */

public class PictureManager extends AppCompatActivity {

    File  single_picture = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 会将整个目录删除
        return dir.delete();
    }

    public void delSingle_picture()
    {
        deleteDir(single_picture);
    }

}
