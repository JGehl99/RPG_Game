package com.mygdx.rpg_game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Anim (path: String, fps: Float){

    // TextureAtlas holds the animations files
    private var textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal(path))
    // Create animation from textureAtlas at set fps
    private var animation: Animation<TextureRegion> = Animation(fps, textureAtlas.regions)
    // elapsed to keep track of current frame
    private var elapsedTime: Float = 0f

    var width: Float = 0f
    var height: Float = 0f

    init {
        //Get width and height of sprite
        width = textureAtlas.regions[0].regionWidth.toFloat()
        height = textureAtlas.regions[0].regionHeight.toFloat()
    }

    fun getNextFrame(): TextureRegion? {
        // Update elapsedTime and return current animation frame
        elapsedTime += Gdx.graphics.deltaTime
        return animation.getKeyFrame(elapsedTime,true)
    }

    fun dispose(){
        textureAtlas.dispose()
    }
}