package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class Joystick(private val player: Player) : InputProcessor {

    // Flag to check if the user has kept their finger on the screen and dragged
    private var isTouched = false
    private var origPoint: Vector2 = Vector2()//pos of initial touch
    private var currentPoint: Vector2 = Vector2()//pos of current touch
    private var oToCurrent: Vector2 = Vector2()//vector from origPoint to currentPoint
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private var largeCircleRadius = 200f
    private var smallCircleRadius = 25f

    private var deadzoneThreshold = 0.3f
    private var deadzoneRadius = deadzoneThreshold * largeCircleRadius
    private var outerRecip = 1.0f / (largeCircleRadius - deadzoneRadius)

    // Function to handle adding this Joystick as an InputProcessor
    fun addInputProcessors(multiplexer: InputMultiplexer) {
        multiplexer.addProcessor(this)
    }

    // When keyboard key (or power and volume on android) is pressed down
    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    // When keyboard key (or power and volume on android) is pressed up
    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    // When keyboard key (or power and volume on android) is pressed down and up
    override fun keyTyped(character: Char): Boolean {
        return true
    }

    // When screen is touched
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        // Set flag
        isTouched = true

        // Store original touch point
        origPoint = Vector2(screenX.toFloat(), screenY.toFloat())

        return true
    }

    // When screen touch is released
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        // Reset flag
        isTouched = false

        // Set player velocity to 0
        player.velocity.x = 0f
        player.velocity.y = 0f

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {

        // If finger is being dragged across screen
        if (isTouched) {

            // Create Vector2 to hold the current touch point
            currentPoint = Vector2(screenX.toFloat(), screenY.toFloat())


            oToCurrent = currentPoint.cpy().sub(origPoint)
            var o2cMag = oToCurrent.len()

            //generate velocity scalar
            var velocityScalar: Float = if(o2cMag > largeCircleRadius){
                1f
            }else{
                (0.5f * (o2cMag - deadzoneRadius) + 0.5f * kotlin.math.abs(o2cMag - deadzoneRadius)) * outerRecip
            }

            val scalingFactor = 1.0f/o2cMag
            player.velocity.x = oToCurrent.x * scalingFactor * velocityScalar * player.maxVelocity
            player.velocity.y = -oToCurrent.y * scalingFactor * velocityScalar * player.maxVelocity

            // Gdx.app.log("INFO", "Player Velocity: ${player.velocity}")

            val a = Vector2(1f, -1f)//up-right
            val b = Vector2(1f, 1f)//down-right

            val aDotDir = a.dot(oToCurrent)
            val bDotDir = b.dot(oToCurrent)

            when (true) {
                (aDotDir >= 0f && bDotDir >=0) -> {
                    player.state = player.stateRight
                }
                (aDotDir >= 0f && bDotDir < 0) -> {
                    player.state = player.stateUp
                }
                (aDotDir < 0f && bDotDir >= 0) -> {
                    player.state = player.stateDown
                }
                (aDotDir < 0f && bDotDir < 0) -> {
                    player.state = player.stateLeft
                }
            }//when
        }
        return true
    }


    // If mouse moved
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    // If mouse scrolled
    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }

    // Render function to draw joystick on screen
    fun render() {

        // Only render the joystick if the User is touching the screen
        if (isTouched) {

            val maxDist = largeCircleRadius - smallCircleRadius

            // Create a Vector2 to store the location of the smallCircle relative to the centre of
            // the largeCircle, clamp from -maxDist to maxDist
            val relativeSmallCircleVector2 =
                Vector2(currentPoint.x - origPoint.x, currentPoint.y - origPoint.y).clamp(
                    -maxDist,
                    maxDist
                )

            // Enable blending to allow for transparent circles
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

            // Begin drawing
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

            // Set colour(rgba) of first circle and draw it
            shapeRenderer.color = Color(0.5f, 0.5f, 0.5f, 0.4f)
            shapeRenderer.circle(
                origPoint.x,
                Gdx.graphics.height.toFloat() - origPoint.y,
                largeCircleRadius)

            // Set colour(rgba) of second circle and draw it
            shapeRenderer.color = Color(0.1f, 0.1f, 0.1f, 0.5f)
            shapeRenderer.circle(
                origPoint.x + relativeSmallCircleVector2.x,
                Gdx.graphics.height.toFloat() - (origPoint.y + relativeSmallCircleVector2.y),
                smallCircleRadius
            )
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }
    }
}