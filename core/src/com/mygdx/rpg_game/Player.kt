package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
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
 * @author Joshua Gehl
 */

class Player(var x: Float = 0f, var y: Float = 0f) {

    private var animations: HashMap<String, Anim> = HashMap()

    private var spriteBatch: SpriteBatch = SpriteBatch()

    private var currentAnimation: Anim

    val stateIdle = 0
    val stateLeft = 1
    val stateRight = 2
    val stateUp = 3
    val stateDown = 4
    val stateAttack = 5
    val stateHit = 6

    var state = 0

    var velocity = Vector2()

    init {
        val files: List<FileHandle> = Gdx.files.internal("anim/player/").list().filter {
            it.extension() == "atlas"
        }
        for (file: FileHandle in files) {
            animations[file.nameWithoutExtension()] = Anim(file.name(), 1/10f)
            Gdx.app.log("INFO", "Created Animation  ${file.nameWithoutExtension()}")
        }

        currentAnimation = animations["idle"]!!
    }

    /**
     * Updates Player's coordinates and draws the sprite
     *
     * @param combined Projection Matrix
     */

    fun update(combined: Matrix4) {

        this.x += velocity.x * Gdx.graphics.deltaTime
        this.y += velocity.y * Gdx.graphics.deltaTime
        // Gdx.app.log("Info", "State: $state")

        currentAnimation = when(state) {
            0 -> animations["idle"]!!
            1 -> animations["walkLeft"]!!
            2 -> animations["walkRight"]!!
            3 -> animations["walkUp"]!!
            4 -> animations["walkDown"]!!
            else -> animations["idle"]!!
        }

        spriteBatch.projectionMatrix = combined
        spriteBatch.begin()
        spriteBatch.draw(currentAnimation.getNextFrame(), this.x - 8, this.y - 8)
        spriteBatch.end()
    }

    /**
     * Dispose class to dispose of anything that can be disposed
     */

    fun dispose() {
        spriteBatch.dispose()
    }
}