package com.mygdx.rpg_game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2

/**
 * Root class for any sort of Entity created in the game. Loads, stores, and renders animations.
 *
 * @param pos Current x and y position of Entity
 * @param spritePath FileHandle of folder containing sprite data
 *
 * @constructor Assigns [pos] and loads animations
 *
 * @property pos Current x and y position of Entity
 *
 * @author Joshua Gehl
 */

abstract class Entity(var pos: Vector2, spritePath: FileHandle) {

    /** Loads Sprites */
    init { loadAnimations(spritePath) }

    /** HashMap containing Anim objects */
    protected var animations: HashMap<Int, Anim> = HashMap()

    /** SpriteBatch for rendering the Player's animations */
    private var spriteBatch: SpriteBatch = SpriteBatch()

    /** CurrentAnimation */
    protected lateinit var currentAnimation: Anim

    /**
     * Load all .atlas files from the supplied [FileHandle], create [Anim] objects for each animation,
     * and add them to a [HashMap]<[Int], [Anim]>. Set [currentAnimation] to first loaded anim.
     *
     * @param spritePath FileHandle to folder containing sprite data
     *
     * @author Joshua Gehl
     */
    private fun loadAnimations(spritePath: FileHandle) {
        var count = 0
        spritePath.list().forEach {
            if (it.extension() == "atlas") {
                animations[count++] = Anim(spritePath.path() + "/" + it.name(), 1/10f)
                Gdx.app.log("Entity", "Created Animation: ${it.nameWithoutExtension()}")
            }
        }
        currentAnimation = animations[0]!!
    }

    /**
     * Set the [spriteBatch]'s View Projection matrix to the one passed in. Draw next frame of
     * [currentAnimation] at [pos].x, [pos].y, offset by half the [currentAnimation]'s width.
     *
     * @param combined View Projection matrix from CustomCamera
     *
     * @author Joshua Gehl
     */
    open fun render(combined: Matrix4) {
        spriteBatch.projectionMatrix = combined
        spriteBatch.begin()
        spriteBatch.draw(
            currentAnimation.getNextFrame(),
            this.pos.x - currentAnimation.width/2,
            this.pos.y - currentAnimation.width/2
        )
        spriteBatch.end()
    }

    /**
     * Disposes of objects that are no longer required.
     *
     * @author Joshua Gehl
     */
    open fun dispose() {
        spriteBatch.dispose()
        currentAnimation.dispose()
        animations.forEach { it.value.dispose()}
    }
}
