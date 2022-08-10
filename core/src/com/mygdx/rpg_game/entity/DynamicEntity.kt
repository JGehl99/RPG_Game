package com.mygdx.rpg_game.entity

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2

abstract class DynamicEntity(pos: Vector2,
                    spritePath: FileHandle,
                    var velocity: Vector2 = Vector2()
) : Entity(
    pos,
    spritePath
) {

    override fun render(combined: Matrix4) {
        this.pos.add(velocity)
        super.render(combined)
    }
}