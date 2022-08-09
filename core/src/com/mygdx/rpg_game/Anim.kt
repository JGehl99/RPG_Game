package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Anim (filename: String, fps: Float){

    private var textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal("anim/player/$filename"))
    private var animation: Animation<TextureRegion> = Animation(fps, textureAtlas.regions)
    private var elapsedTime: Float = 0f

    fun getNextFrame(): TextureRegion? {
        elapsedTime += Gdx.graphics.deltaTime
        return animation.getKeyFrame(elapsedTime,true)
    }

    fun dispose(){
        textureAtlas.dispose()
    }
}