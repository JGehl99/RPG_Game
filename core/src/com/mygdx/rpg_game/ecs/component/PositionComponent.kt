package com.mygdx.rpg_game.ecs.component

import com.badlogic.gdx.math.Vector2

interface PositionComponent {
    var pos: Vector2
    fun setPosition(pos: Vector2): Vector2
}