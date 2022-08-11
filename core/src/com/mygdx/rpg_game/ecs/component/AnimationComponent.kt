package com.mygdx.rpg_game.ecs.component

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mygdx.rpg_game.entity.Anim
import com.mygdx.rpg_game.entity.player.PlayerState
import java.util.*

interface AnimationComponent {
    var animConfig: String
    var animations: EnumMap<PlayerState, Anim>
    var spriteBatch: SpriteBatch
    var currentAnimation: Anim
    fun loadAnimations()
    fun getCurrentFrame(): TextureRegion?
}