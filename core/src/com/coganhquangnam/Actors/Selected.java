package com.haminhon.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.haminhon.gesture.Assets;


/**
 * Created by nguyen gon on 2015/12/26.
 */
public class Selected extends Actor {

    private static final float GOLDEN_RATIO = 1.68f;
    private static final Texture texture  = Assets.selectedSign;


    private  float conMoiSize;
    private ChessPiece selectedChessPiece;


    public Selected(ChessPiece chessPiece)
    {

        this.selectedChessPiece = chessPiece;
        this.conMoiSize = chessPiece.size * GOLDEN_RATIO;

    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(texture, selectedChessPiece.getScreenCoord().x - conMoiSize / 2, selectedChessPiece.getScreenCoord().y - conMoiSize / 2, conMoiSize, conMoiSize);
    }

}
