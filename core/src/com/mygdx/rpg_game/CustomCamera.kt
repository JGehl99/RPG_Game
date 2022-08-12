package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera

/**
 * CustomCamera class that extends OrthographicCamera(). CustomCamera allows for the camera to
 * be clamped to the edge of the map.
 *
 * @param vpWidth Width of viewport in pixels
 * @param vpHeight Height of viewport in pixels
 *
 * @constructor Sets the camera to ortho and calls update()
 *
 * @author Joshua Gehl
 */

class CustomCamera(vpWidth: Float, vpHeight: Float): OrthographicCamera() {

    init {
        this.setToOrtho(
            false,
            vpWidth * (Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()),
            vpHeight
        )
        this.update()
    }

    /**
     * Calls [update] using the players coordinates
     *
     * @param player Player object
     * @param mapWidth Width of the map
     * @param mapHeight Height of the map
     *
     * @author Joshua Gehl
     */
    fun followPlayer(player: com.mygdx.rpg_game.ecs.entity.Player, mapWidth: Float, mapHeight: Float) {
        this.update(player.pos.x, player.pos.y, mapWidth, mapHeight)
    }

    /**
     * Moves the camera to x, y, but clamps the viewport to the edges of the map. This prevents
     * the camera from extending past the map edge and showing black screen.
     *
     * @param x x position of camera
     * @param y y position of camera
     * @param mapWidth Width of the map
     * @param mapHeight Height of the map
     *
     * @author Joshua Gehl
     */
    fun update(x: Float, y: Float, mapWidth: Float, mapHeight: Float) {

        val vpw2: Float = this.viewportWidth/2f
        val vph2: Float = this.viewportHeight/2f

        if (x > vpw2 && x < (mapWidth - vpw2)) {
            this.position.x = x
        }

        if (y > vph2 && y < (mapHeight - vph2)) {
            this.position.y = y
        }

        if(this.position.y - vph2 < 0){
            this.position.y = 0 + vph2
        }
        if(this.position.y + vph2 > mapHeight){
            this.position.y = mapHeight - vph2
        }
        if(this.position.x - vpw2 < 0){
            this.position.x = 0 + vpw2
        }
        if(this.position.x + vpw2 > mapWidth){
            this.position.x = mapWidth - vpw2
        }

        super.update()
    }
}