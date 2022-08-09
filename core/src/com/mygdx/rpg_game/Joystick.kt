package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class Joystick(private val player:Player): InputProcessor {

    private var isTouched = false
    private var origPoint: Vector2 = Vector2()
    private var currentPoint: Vector2 = Vector2()
    private var directionVec: Vector2 = Vector2()
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()

    fun addInputProcessors(multiplexer: InputMultiplexer){
        multiplexer.addProcessor(this)
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

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouched = true
        origPoint = Vector2(screenX.toFloat(), screenY.toFloat())
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouched = false

        // player.state = player.stateIdle
        player.velocity.x = 0f
        player.velocity.y = 0f

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (isTouched) {

            currentPoint = Vector2(screenX.toFloat(), screenY.toFloat())

            directionVec = currentPoint.cpy().sub(origPoint)
            directionVec.nor()

            player.velocity.x = directionVec.x * 16f * 5
            player.velocity.y = -directionVec.y * 16f * 5

            // Gdx.app.log("INFO", "Player Velocity: ${player.velocity}")

            val a = Vector2(1f, 1f)
            val b = Vector2(1f, -1f)

            val aDotDir = a.dot(directionVec)
            val bDotDir = b.dot(directionVec)

            when (true) {
                (aDotDir >= 0f && bDotDir >=0) -> {
                    player.state = player.stateRight
                }
                (aDotDir >= 0f && bDotDir < 0) -> {
                    player.state = player.stateDown
                }
                (aDotDir < 0f && bDotDir >= 0) -> {
                    player.state = player.stateUp
                }
                (aDotDir < 0f && bDotDir < 0) -> {
                    player.state = player.stateLeft
                }
            }
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }

    fun render() {
        if(isTouched) {

            val largeCircleRadius = 100f
            val smallCircleRadius = 25f
            val maxDist = largeCircleRadius - smallCircleRadius

            val relativeSmallCircleVector2 = Vector2(currentPoint.x - origPoint.x, currentPoint.y - origPoint.y).clamp(-maxDist, maxDist)

            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

            shapeRenderer.color = Color(0.4f, 0.4f, 0.4f, 0.7f)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.circle(origPoint.x, (Gdx.graphics.height.toFloat() - origPoint.y), largeCircleRadius)
            shapeRenderer.color = Color(0.0f, 0.0f, 0.0f, 0.5f)
            shapeRenderer.circle(origPoint.x + relativeSmallCircleVector2.x, (Gdx.graphics.height.toFloat() - (origPoint.y + relativeSmallCircleVector2.y)), smallCircleRadius)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }
    }
}