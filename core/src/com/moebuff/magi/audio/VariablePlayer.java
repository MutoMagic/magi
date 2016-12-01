package com.moebuff.magi.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.io.Decoder;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.transform.SoundTouch;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.io.BufferUtils;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.MP3Decoder;

import java.nio.ByteBuffer;

import static com.moebuff.magi.audio.Status.*;

/**
 * 伸缩与变调
 *
 * @author muto
 */
public class VariablePlayer implements Player, Runnable {
    private static final int bufferSize = 4096 * 10;
    private static final int bufferCount = 3;
    private static final int bytesPerSample = 2;
    private static final byte[] tempBytes = new byte[bufferSize];
    private static final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(bufferSize);

    private FileHandle handle;
    private float rate;
    private boolean tone;
    private OnCompletionListener listener;
    private Status status = STOP;
    private Thread playback = new Thread(this);

    private Bitstream bitstream;
    private MP3Decoder decoder;
    private SoundTouch touch;
    private AudioDevice device;
    private float volume = 1.0f;
    private int position;

    public VariablePlayer(FileHandle handle, float rate, boolean tone) {
        this.handle = handle;
        this.rate = rate;
        this.tone = tone;

        playback.setDaemon(true);
        bitstream = new Bitstream(handle.read());
        decoder = new MP3Decoder();
        touch = new SoundTouch();
    }

    @Override
    public void play() {
        playback.start();
        status = PLAY;
    }

    @Override
    public void play(int position) {
        setPosition(position);
        play();
    }

    @Override
    public void pause() {
        status = PAUSE;
    }

    @Override
    public void stop() {
        status = STOP;
    }

    @Override
    public void dispose() {
        touch.dispose();
        device.dispose();
        status = DISPOSE;
        listener = null;
        position = 0;
    }

    @Override
    public int getVolume() {
        return (int) (volume * 100);
    }

    @Override
    public void setVolume(int per) {
        device.setVolume(volume = per / 100);
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setListener(OnCompletionListener listener) {
        this.listener = listener;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return status.isPlaying();
    }

    @Override
    public void run() {
        Decoder decoder = new Mpg123Decoder(handle);
        int channels = decoder.getChannels();

        touch.setChannels(channels);
        touch.setSampleRate(decoder.getRate());
        if (tone) {
            touch.setRate(rate);
        } else {
            touch.setTempo(rate);
        }

        short[] samples = new short[20480];//11个1024足以
        int length, max = samples.length / channels;
        device = Gdx.audio.newAudioDevice(decoder.getRate(), channels == 1);

        while ((length = decoder.readSamples(samples, 0, samples.length)) > 0) {
            touch.putSamples(samples, 0, length / channels);
            if ((length = touch.receiveSamples(samples, 0, max)) > 0) {
                device.writeSamples(samples, 0, length * channels);
            }
        }

        if (listener != null) {
            listener.onCompletion(this);
        }
    }
}
