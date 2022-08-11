package com.mygdx.rpg_game.ecs.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World

interface Box2DComponent {
    var bodyDef: BodyDef
    var body: Body
    var fixtureDef: FixtureDef
    fun setupBody(world: World)
}