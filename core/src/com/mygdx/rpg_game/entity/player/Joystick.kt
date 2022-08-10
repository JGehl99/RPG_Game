package com.mygdx.rpg_game.entity.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class Joystick{

    // Render function to draw joystick on screen
    fun render(originalPoint: Vector2, currentPoint: Vector2) {

        val shapeRenderer = ShapeRenderer()

        // Define circle radii and maxDist for the small circle to travel
        val largeCircleRadius = 100f
        val smallCircleRadius = 25f
        val maxDist = largeCircleRadius - smallCircleRadius

        // Create a Vector2 to store the location of the smallCircle relative to the centre of
        // the largeCircle, clamp from -maxDist to maxDist
        val relativeSmallCircleVector2 =
            Vector2(currentPoint.x - originalPoint.x, currentPoint.y - originalPoint.y).clamp(
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
}