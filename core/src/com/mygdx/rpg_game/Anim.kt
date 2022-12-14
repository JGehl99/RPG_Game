package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.TimeUtils

/**
 * [Animation] wrapper class. Simplifies loading a TextureAtlas and Animation.
 *
 * @param path Asset path to Animation files
 * @param fps Frame rate of Animation
 *
 * @constructor Defines width and height of the Animation in pixels.
 *
 * @author Joshua Gehl
 */
class Anim (path: String, fps: Float){

    // TextureAtlas holds the animations files
    private var textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal(path))
    // Create animation from textureAtlas at set fps
    private var animation: Animation<TextureRegion> = Animation(fps, textureAtlas.regions)

    private var elapsedTime: Float = 0f

    var width: Float = 0f
    var height: Float = 0f

    init {
        //Get width and height of sprite
        width = textureAtlas.regions[0].regionWidth.toFloat()
        height = textureAtlas.regions[0].regionHeight.toFloat()
    }

    /**
     * Returns the next frame at time elapsedTime + deltaTime.
     *
     * @return Returns the next frame of the animation
     *
     * @author Joshua Gehl
     */
    fun getNextFrame(): TextureRegion? {
        elapsedTime += Gdx.graphics.deltaTime
        return animation.getKeyFrame(elapsedTime,true)
    }

    /** Dispose of any objects that are no longer required */
    fun dispose(){
        textureAtlas.dispose()
    }
}