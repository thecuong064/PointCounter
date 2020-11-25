package com.thecuong064.pointcounter.listener;

import android.content.Context;
import android.media.AudioManager;
import android.view.SoundEffectConstants;
import android.view.View;

public abstract class OnClickListenerWithSound implements View.OnClickListener {
    AudioManager audioManager;

    @Override
    public void onClick(View v) {
        audioManager = (AudioManager) v.getContext().getSystemService(Context.AUDIO_SERVICE);
        playClickSound();
        onClickWithSound(v);
    }

    private void playClickSound() {
        if (audioManager != null) {
            float currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.playSoundEffect(SoundEffectConstants.CLICK, currentVol/maxVol);
        }
    }

    public abstract void onClickWithSound(View v);
}
