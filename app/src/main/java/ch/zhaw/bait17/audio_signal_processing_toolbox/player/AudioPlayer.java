package ch.zhaw.bait17.audio_signal_processing_toolbox.player;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by georgrem, stockan1 on 11.03.2017.
 */

public interface AudioPlayer {


    void init(Context context, PlaybackListener listener);

    /**
     * Starts the audio playback.
     */
    void play(String uri);

    /**
     * Pauses the audio playback.
     */
    void pause();

    /**
     * Stops the audio playback.
     */
    void stop();

    /**
     * Returns true if the AudioTrack play state is PlAYSTATE_PLAYING.
     */
    boolean isPlaying();

    /**
     * Returns true if the AudioTrack play state is PLAYSTATE_PAUSED.
     */
    boolean isPaused();

    /**
     * Releases the player and frees up all the associated resources.
     */
    void release();

    /**
     * Returns a string representing the name of the currently selected track.
     */
    @Nullable
    String getCurrentTrack();

    /**
     * Retuns the sample rate of the currently selected/played track.
     */
    int getSampleRate();

    /**
     * Retuns the number of audio channels of the currently selected/played track.
     */
    int getChannels();

    /**
     * Places the playback head to the new position within the track.
     * @param msec The position expressed in milliseconds.
     */
    void seekToPosition(int msec);

    /**
     * Returns the current position in milliseconds.
     */
    int getCurrentPosition();

}
