package com.haminhon.gesture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by nguyen gon on 2016/03/08.
 */
public class Assets {

    public  static  Texture darkSide, lightSide;
    public static   Texture selectedSign;
    public static  Texture playBackImage;
    public static Sound openSound, moveSound, hitSound;

    public static void load()
    {
        playBackImage = new Texture(Gdx.files.internal("data/back.png"));
        darkSide = new Texture(Gdx.files.internal("data/dark.png"));
        lightSide =  new Texture(Gdx.files.internal("data/light.png"));
        selectedSign = new Texture(Gdx.files.internal("data/sheep.png"));
        openSound = Gdx.audio.newSound(Gdx.files.internal("data/open.mp3"));
        moveSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
    }

    public static void playSound(Sound sound)
    {
        sound.play(1);
    }


}
