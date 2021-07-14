package com.fly.audio.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fly.core.base.BaseApplicationKt.appContext;

public class FileUtil {
    //原始文件(不能播放)
    private final static String AUDIO_PCM_PATH = "/pcm/";
    //可播放的高质量音频文件
    private final static String AUDIO_WAV_PATH = "/wav/";


    public static String getPcmFileAbsolutePath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("fileName isEmpty");
        }
        if (!isSdcardExit()) {
            throw new IllegalStateException("sd card no found");
        }
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".pcm")) {
                fileName = fileName + ".pcm";
            }
            String fileBasePath = appContext.getCacheDir() + AUDIO_PCM_PATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName;
        }

        return mAudioRawPath;
    }

    public static String getWavFileAbsolutePath(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("fileName can't be null");
        }
        if (!isSdcardExit()) {
            throw new IllegalStateException("sd card no found");
        }

        String mAudioWavPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".wav")) {
                fileName = fileName + ".wav";
            }
            String fileBasePath = appContext.getCacheDir() + AUDIO_WAV_PATH;
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioWavPath = fileBasePath + fileName;
        }
        return mAudioWavPath;
    }

    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取全部pcm文件列表
     *
     * @return
     */
    public static List<File> getPcmFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = appContext.getCacheDir() + AUDIO_PCM_PATH;

        File rootFile = new File(fileBasePath);
        if (rootFile.exists()) {
            File[] files = rootFile.listFiles();
            Collections.addAll(list, files);

        }
        return list;

    }

    /**
     * 获取全部wav文件列表
     *
     * @return
     */
    public static List<File> getWavFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = appContext.getCacheDir() + AUDIO_WAV_PATH;

        File rootFile = new File(fileBasePath);
        if (rootFile.exists()) {
            File[] files = rootFile.listFiles();
            Collections.addAll(list, files);

        }
        return list;
    }
}
