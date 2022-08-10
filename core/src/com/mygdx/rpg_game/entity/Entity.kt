package com.mygdx.rpg_game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2

abstract class Entity {

    constructor(pos: Vector2, spritePath: FileHandle) {
        this.pos = pos
        this.animations = HashMap()
        this.spriteBatch = SpriteBatch()
        loadSprites(spritePath)
    }

    constructor(x: Float = 0f, y: Float = 0f, spritePath: FileHandle) {
        this.pos.x = x
        this.pos.y = y
        this.animations = HashMap()
        this.spriteBatch = SpriteBatch()
        loadSprites(spritePath)
    }

    var pos: Vector2 = Vector2()

    // HashMap to hold Anim objects
    protected var animations: HashMap<Int, Anim>

    // SpriteBatch for rendering the Player's animations
    private var spriteBatch: SpriteBatch

    // Current Animations
    protected lateinit var currentAnimation: Anim

    private fun loadSprites(spritePath: FileHandle) {
        var count = 0
        // Retrieve the animations files as a list, set currentAnimation to first animation
        spritePath.list().forEach {
            if (it.extension() == "atlas") {
                animations[count++] = Anim(spritePath.path() + "/" + it.name(), 1/10f)
                Gdx.app.log("Entity", "Created Animation: ${it.nameWithoutExtension()}")
            }
        }
        currentAnimation = animations[0]!!
    }

    open fun render(combined: Matrix4) {

        // Sets spriteBatch's proj matrix to the one passed in, from camera
        spriteBatch.projectionMatrix = combined
        spriteBatch.begin()
        spriteBatch.draw(
            currentAnimation.getNextFrame(),
            this.pos.x - currentAnimation.width/2,
            this.pos.y - currentAnimation.width/2
        )
        spriteBatch.end()
    }

    open fun dispose() {
        spriteBatch.dispose()
        currentAnimation.dispose()
        animations.forEach { it.value.dispose()}
    }
}


