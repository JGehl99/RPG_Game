package com.mygdx.rpg_game.entity

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2

/**
 * Extended Entity class that adds a velocity vector and allows for the DynamicEntity to move.
 *
 * @param pos Current x and y position of DynamicEntity
 * @param spritePath FileHandle of folder containing sprite data
 * @param velocity Current velocity vector
 *
 * @constructor Assigns [velocity]
 *
 * @author Joshua Gehl
 */
abstract class DynamicEntity(pos: Vector2, spritePath: FileHandle,  var velocity: Vector2 = Vector2()):
    Entity(pos, spritePath) {

    /**
     * Overridden render function that applies current velocity to DynamicEntity
     *
     * @param combined View Projection matrix from CustomCamera
     *
     * @author Joshua Gehl
     */
    override fun render(combined: Matrix4) {
        this.pos.add(velocity)
        super.render(combined)
    }
}