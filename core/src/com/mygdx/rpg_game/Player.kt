package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2




/**
 * Simple Player class consisting of x, y, and sprite.
 *
 * @param x x coordinate
 * @param y y coordinate
 *
 * @property sprite Holds Player's [Sprite]
 * @property spriteBatch [SpriteBatch] for rendering Player's [Sprite]
 *
 * @author Joshua Gehl
 */

class Player(var x: Float = 0f, var y: Float = 0f) {

    private var sprite: Sprite = Sprite(Texture(Gdx.files.internal("7soulsrpggraphics_sprites_V3/RPG Graphics Pack - Sprites/Human (Front)/Full/player_85.png")))
    private var spriteBatch: SpriteBatch = SpriteBatch()

    val stateIdle = 0
    val stateLeft = 1
    val stateRight = 2
    val stateUp = 3
    val stateDown = 4
    val stateAttack = 5
    val stateHit = 6

    var state = 0

    var velocity = Vector2()

    /**
     * Updates Player's coordinates and draws the sprite
     *
     * @param combined Projection Matrix
     */

    fun update(combined: Matrix4) {

        this.x += velocity.x * Gdx.graphics.deltaTime
        this.y += velocity.y * Gdx.graphics.deltaTime
        // Gdx.app.log("Info", "State: $state")

        spriteBatch.projectionMatrix = combined
        spriteBatch.begin()
        spriteBatch.draw(sprite, this.x - sprite.width/2, this.y - sprite.height/2)
        spriteBatch.end()
    }

    /**
     * Dispose class to dispose of anything that can be disposed
     */

    fun dispose() {
        spriteBatch.dispose()
    }
}