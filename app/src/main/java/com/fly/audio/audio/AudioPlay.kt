package com.fly.audio.audio

import android.media.*
import android.util.Log
import java.io.*

/**
 * Created by likainian on 2021/7/13
 * Description:  播放的工具
 */

object AudioPlay{

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private const val AUDIO_SAMPLE_RATE = 16000

    //编码
    private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

    private const val AUDIO_CONF = AudioFormat.CHANNEL_OUT_MONO
    fun playPcm(file: File) {
        Log.d("AudioRecorder", "===play===")
        Thread {
            try {
                //实例AudioTrack
                val minBuffSize =
                    AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CONF, AUDIO_ENCODING)
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AUDIO_ENCODING)
                            .setSampleRate(AUDIO_SAMPLE_RATE)
                            .setChannelMask(AUDIO_CONF)
                            .build()
                    )
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(minBuffSize)
                    .build()
                val bytes = ByteArray(minBuffSize)
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                val dis = DataInputStream(BufferedInputStream(FileInputStream(file)))

                //开始播放
                track.play()
                var len: Int
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while ((dis.read(bytes).also { len = it }) != -1) {
                    track.write(bytes, 0, len)
                }
                //播放结束
                dis.close()
                track.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun play(file: File){
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }
}