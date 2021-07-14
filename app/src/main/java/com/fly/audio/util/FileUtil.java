package com.fly.audio.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fly.core.base.BaseApplicationKt.appContext;

public class FileUtil {
    //原始文件
    private final static String AUDIO_PCM_PATH = "/pcm/";
    //可播放的高质量音频文件
    private final static String AUDIO_WAV_PATH = "/wav/";

    private final static String AUDIO_IMAGE_PATH = "/image/";

    public static String getAbsolutePath(String fileName) {
        String fileBasePath = appContext.getCacheDir() + AUDIO_IMAGE_PATH;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }

        return fileBasePath + fileName;
    }

    public static String getPcmFileAbsolutePath(String fileName) {
        String fileBasePath = appContext.getCacheDir() + AUDIO_PCM_PATH;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }

        return fileBasePath + fileName;
    }

    public static String getWavFileAbsolutePath(String fileName) {
        if (!fileName.endsWith(".wav")) {
            fileName = fileName + ".wav";
        }
        String fileBasePath = appContext.getCacheDir() + AUDIO_WAV_PATH;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileBasePath + fileName;
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
