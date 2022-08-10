package com.mygdx.rpg_game.entity.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.mygdx.rpg_game.entity.DynamicEntity

/**
 * Player class that extends DynamicEntity to allow for movement. Player class handles the Player's
 * states and inputs and adjusts the velocity and animation accordingly.
 *
 * @param pos Current x and y position of DynamicEntity
 * @param spritePath FileHandle of folder containing sprite data
 *
 * @author Joshua Gehl
 */
class Player(pos: Vector2, spritePath: FileHandle) : DynamicEntity(pos, spritePath),
    InputProcessor {

    private var playerState: PlayerState = PlayerState.STATE_DOWN     // Current state
    private var isTouched = false                   // Flag to check if screen is touched
    private var originalPoint: Vector2 = Vector2()  // First xy point that user touched screen
    private var currentPoint: Vector2 = Vector2()   // Currently touched xy point
    private var dirVec: Vector2 = Vector2()         // Direction vec: currentPoint - OriginalPoint
    private val joystick: Joystick = Joystick()     // Class to draw joystick on screen

    /** Update the current animation of the Player based on playerState. */
    private fun updateAnimation() {
        // Sets current animations based on current state
        currentAnimation = when (playerState) {
            PlayerState.STATE_IDLE -> animations[PlayerState.STATE_IDLE.ordinal]!!
            PlayerState.STATE_DOWN -> animations[PlayerState.STATE_DOWN.ordinal]!!
            PlayerState.STATE_LEFT -> animations[PlayerState.STATE_LEFT.ordinal]!!
            PlayerState.STATE_RIGHT -> animations[PlayerState.STATE_RIGHT.ordinal]!!
            PlayerState.STATE_UP -> animations[PlayerState.STATE_UP.ordinal]!!
        }
    }

    /**
     * Overridden render function that applies current velocity to DynamicEntity
     *
     * @param combined View Projection matrix from CustomCamera
     *
     * @author Joshua Gehl
     */
    override fun render(combined: Matrix4) {
        this.updateAnimation()
        super.render(combined)
        if (isTouched) {
            joystick.render(originalPoint, currentPoint)
        }
    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        // Set flag
        isTouched = true

        // Store original touch point
        originalPoint = Vector2(screenX.toFloat(), screenY.toFloat())

        return true
    }

    // When screen touch is released
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        // Reset flag
        isTouched = false

        // Set player velocity to 0
        this.velocity = Vector2(0f, 0f)

        return true
    }


    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {

        // If finger is being dragged across screen
        if (isTouched) {

            // Create Vector2 to hold the current touch point
            currentPoint = Vector2(screenX.toFloat(), screenY.toFloat())

            // Create direction Vector2 between origPoint and currentPoint
            dirVec = currentPoint.cpy().sub(originalPoint)

            // Normalize vector
            dirVec.nor()

            Gdx.app.log("Info", "dirVec: $dirVec")

            // Scale direction vec and adjust the player's velocity accordingly
            this.velocity.x = dirVec.x * 1.25f
            this.velocity.y = -dirVec.y * 1.25f

            // Gets dot product of direVec with diagonal vector in an X shape at center of largeCircle
            val aDotDir = Vector2(1f, 1f).dot(dirVec)
            val bDotDir = Vector2(1f, -1f).dot(dirVec)

            /** @author Joshua Gehl, Travis Day */
            when (true) {
                // + + = moving right
                (aDotDir >= 0f && bDotDir >= 0) -> {
                    this.playerState = PlayerState.STATE_RIGHT
                }
                // + - = moving down
                (aDotDir >= 0f && bDotDir < 0) -> {
                    this.playerState = PlayerState.STATE_DOWN
                }
                // - + = moving up
                (aDotDir < 0f && bDotDir >= 0) -> {
                    this.playerState = PlayerState.STATE_UP
                }
                // - - = moving left
                (aDotDir < 0f && bDotDir < 0) -> {
                    this.playerState = PlayerState.STATE_LEFT
                }
            }
        }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        return true
    }
    override fun keyUp(keycode: Int): Boolean {
        return true
    }
    override fun keyTyped(character: Char): Boolean {
        return true
    }
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }
    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }
}