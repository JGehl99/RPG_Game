package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Anim (filename: String, fps: Float){

    // TextureAtlas holds the animations files
    private var textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal("anim/player/$filename"))
    // Create animation from textureAtlas at set fps
    private var animation: Animation<TextureRegion> = Animation(fps, textureAtlas.regions)
    // elapsed to keep track of current frame
    private var elapsedTime: Float = 0f

    fun getNextFrame(): TextureRegion? {
        // Update elapsedTime and return current animation frame
        elapsedTime += Gdx.graphics.deltaTime
        return animation.getKeyFrame(elapsedTime,true)
    }

    fun dispose(){
        textureAtlas.dispose()
    }
}