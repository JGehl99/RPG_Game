package com.mygdx.rpg_game.ecs.component

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface SpriteComponent {

    var spriteBatch: SpriteBatch
    var spritePath: String
    var texture: Texture
}