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

    // Holds coords of original touch point
    private var origPoint: Vector2 = Vector2()

    // Holds coords of current touch point
    private var currentPoint: Vector2 = Vector2()

    // Direction Vector from origPoint to currentPoint
    private var directionVec: Vector2 = Vector2()

    // ShapeRenderer to draw circles for joystick
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()

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

            // Create direction Vector2 between origPoint and currentPoint
            directionVec = currentPoint.cpy().sub(origPoint)

            // Normalize vector
            directionVec.nor()

            // Scale direction vec and adjust the player's velocity accordingly
            player.velocity.x = directionVec.x * 16f * 5
            player.velocity.y = -directionVec.y * 16f * 5

            // Create two direction vectors in an x shape to define the sections for different animations
            val a = Vector2(1f, 1f)
            val b = Vector2(1f, -1f)

            // Calculate dot products a.directionVec and b.directionVec
            val aDotDir = a.dot(directionVec)
            val bDotDir = b.dot(directionVec)

            // + + = moving right
            // + - = moving down
            // - + = moving up
            // - - = moving left
            when (true) {
                (aDotDir >= 0f && bDotDir >= 0) -> {
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

            // Define circle radii and maxDist for the small circle to travel
            val largeCircleRadius = 200f
            val smallCircleRadius = 50f
            val maxDist = largeCircleRadius - smallCircleRadius / 2f

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
            shapeRenderer.circle(origPoint.x, (Gdx.graphics.height.toFloat() - origPoint.y), 200f)

            // Set colour(rgba) of second circle and draw it
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0.1f, 0.1f, 0.1f, 0.5f)
            shapeRenderer.circle(
                origPoint.x + relativeSmallCircleVector2.x,
                (Gdx.graphics.height.toFloat() - (origPoint.y + relativeSmallCircleVector2.y)),
                50f
            )
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }
    }
}