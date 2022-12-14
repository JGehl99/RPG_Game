package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

/**
 * Joystick class to render a joystick on the screen when the User touches the screen.
 *
 * @author Joshua Gehl
 */

class Joystick{

    private val shapeRenderer = ShapeRenderer()
    private val largeCircleRadius = 100f
    private val smallCircleRadius = 25f
    private val maxDist = largeCircleRadius - smallCircleRadius

    private lateinit var relativeSmallCircleVector2: Vector2


    /**
     * Renders two circles on the screen where the player touches that simulate a on-screen joystick.
     *
     * @param originalPoint First point touched by User
     * @param currentPoint Current point touched by User
     *
     * @author Joshua Gehl
     */
    fun render(originalPoint: Vector2, currentPoint: Vector2) {

        // Create a Vector2 to store the location of the smallCircle relative to the centre of
        // the largeCircle, clamp from -maxDist to maxDist
        relativeSmallCircleVector2 = Vector2(
            currentPoint.x - originalPoint.x,
            currentPoint.y - originalPoint.y
        ).clamp(-maxDist, maxDist)

        // Enable blending to allow for transparent circles
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // Begin drawing
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Set colour(rgba) of first circle and draw it
        shapeRenderer.color = Color(0.5f, 0.5f, 0.5f, 0.4f)
        shapeRenderer.circle(originalPoint.x, (Gdx.graphics.height.toFloat() - originalPoint.y), largeCircleRadius)

        // Set colour(rgba) of second circle and draw it
        shapeRenderer.color = Color(0.1f, 0.1f, 0.1f, 0.5f)
        shapeRenderer.circle(
            originalPoint.x + relativeSmallCircleVector2.x,
            (Gdx.graphics.height.toFloat() - (originalPoint.y + relativeSmallCircleVector2.y)),
            smallCircleRadius
        )
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

    }

    fun dispose() {
        shapeRenderer.dispose()
    }
}