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

    // HashMap to hold Anim objects
    private var animations: HashMap<String, Anim> = HashMap()

    // SpriteBatch for rendering the Player's animations
    private var spriteBatch: SpriteBatch = SpriteBatch()

    // Holds the animation to play when rendering
    private var currentAnimation: Anim

    // Player states, two currently not implemented due to lack of animations
    val stateIdle = 0
    val stateLeft = 1
    val stateRight = 2
    val stateUp = 3
    val stateDown = 4
    val stateAttack = 5
    val stateHit = 6

    // Current state
    var state = 0

    // Player velocity
    var velocity = Vector2()

    init {
        // Retrieve the animations files as a list
        val files: List<FileHandle> = Gdx.files.internal("anim/player/").list().filter {
            it.extension() == "atlas"
        }
        // Loop over files and create Anims
        for (file: FileHandle in files) {
            animations[file.nameWithoutExtension()] = Anim(file.name(), 1/10f)
            Gdx.app.log("INFO", "Created Animation  ${file.nameWithoutExtension()}")
        }
        // Sets current animation to
        currentAnimation = animations["idle"]!!
    }

    /**
     * Updates Player's coordinates and draws the sprite
     *
     * @param combined Projection Matrix
     */

    fun update(combined: Matrix4) {

        // Updates players position
        this.x += velocity.x
        this.y += velocity.y

        // Sets current animations based on current state
        currentAnimation = when(state) {
            stateIdle -> animations["idle"]!!
            stateLeft -> animations["walkLeft"]!!
            stateRight -> animations["walkRight"]!!
            stateUp -> animations["walkUp"]!!
            stateDown -> animations["walkDown"]!!
            else -> animations["idle"]!!
        }

        // Sets spriteBatch's proj matrix to the one passed in, from camera
        spriteBatch.projectionMatrix = combined

        spriteBatch.begin()

        // Draw the next frame of the animations at Player's x and y, offsetting for the size of the
        // sprites
        spriteBatch.draw(currentAnimation.getNextFrame(), this.x - 8, this.y - 8)
        spriteBatch.end()
    }

    /**
     * Dispose class to dispose of anything that can be disposed
     */

    fun dispose() {
        animations.forEach {(_, value) -> value.dispose()}
        spriteBatch.dispose()
    }
}