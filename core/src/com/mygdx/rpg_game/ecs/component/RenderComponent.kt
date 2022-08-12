package com.mygdx.rpg_game.ecs.component

import com.mygdx.rpg_game.CustomCamera

interface RenderComponent {
    fun render(camera: CustomCamera)
}