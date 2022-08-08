package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.MathUtils.atan2
import com.badlogic.gdx.math.Vector2

class Joystick(private val player:Player): InputProcessor {

    private var isTouched = false
    private var origPoint: Vector2 = Vector2()
    private var currentPoint: Vector2 = Vector2()
    private var directionVec: Vector2 = Vector2()

    override fun keyDown(keycode: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun keyUp(keycode: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun keyTyped(character: Char): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouched = true
        origPoint = Vector2(screenX.toFloat(), screenY.toFloat())
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouched = false

        player.state = player.stateIdle
        player.velocity.x = 0f
        player.velocity.y = 0f

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (isTouched) {

            currentPoint = Vector2(screenX.toFloat(), screenY.toFloat())

            directionVec = currentPoint.sub(origPoint)
            directionVec.nor()

            //Gdx.app.log("INFO", "Direction Vector: ${directionVec}")
            player.velocity.x = directionVec.x * 16f
            player.velocity.y = -directionVec.y * 16f

            Gdx.app.log("INFO", "Player Velocity: ${player.velocity.nor()}")

            val angle = atan2(player.velocity.x, player.velocity.y) * 180 / 3.14159f

            if (-135 < angle && angle <= -45) {
                player.state = player.stateLeft
            }
            if (45 < angle && angle <= 135) {
                player.state = player.stateRight
            }
            if ((135 < angle && angle <= 180) || (-180 < angle && angle <= -135)) {
                player.state = player.stateDown
            }
            if (-45 < angle && angle <= 45) {
                player.state = player.stateUp
            }
        }

        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        TODO("Not yet implemented")
    }
}