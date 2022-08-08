package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera

/**
 * CustomCamera class that extends OrthographicCamera(). CustomCamera allows for the camera to
 * be clamped to the edge of the map.
 *
 * @param vpWidth Width of viewport in pixels
 * @param vpHeight Height of viewport in pixels
 * @param player Reference to Player
 *
 * @constructor Sets the camera to ortho and calls update()
 *
 *
 *
 * @author Joshua Gehl
 */

class CustomCamera(vpWidth: Float, vpHeight: Float, private var player: Player): OrthographicCamera() {

    init {
        this.setToOrtho(
            false,
            vpWidth * (Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()),
            vpHeight
        )
        this.update()
    }

    /**
     * Clamps camera to the bounds [minX], [minY], [maxX], and [maxY]
     *
     * @param minX Minimum x value for viewport
     * @param minY Minimum y value for viewport
     * @param minY Maximum y value for viewport
     * @param minY Maximum y value for viewport
     */

    fun updateCamera(minX: Float, minY: Float, maxX: Float, maxY: Float) {

        if (player.x > this.viewportWidth/2f && player.x < (maxX - this.viewportWidth/2f)) {
            this.position.x = player.x
        }

        if (player.y > this.viewportHeight/2f && player.y < (maxY - this.viewportHeight/2f)) {
            this.position.y = player.y
        }

        if(this.position.y - this.viewportHeight/2f < minY){
            this.position.y = minY + this.viewportHeight/2f
        }
        if(this.position.y + this.viewportHeight/2f > maxY){
            this.position.y = maxY - this.viewportHeight/2f
        }
        if(this.position.x - this.viewportWidth/2f < minX){
            this.position.x = minX + this.viewportWidth/2f
        }
        if(this.position.x + this.viewportWidth/2f > maxX){
            this.position.x = maxY - this.viewportWidth/2f
        }

        this.update()
    }
}