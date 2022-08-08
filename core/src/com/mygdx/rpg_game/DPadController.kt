package com.mygdx.rpg_game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class DPadController(stage: Stage) {

    private var dpadTable = Table()

    private val listenerLeft = ClickListener()
    private val listenerRight = ClickListener()
    private val listenerUp = ClickListener()
    private val listenerDown = ClickListener()

    fun isMovingLeft(): Boolean {
        return listenerLeft.isPressed
    }
    fun isMovingRight(): Boolean {
        return listenerRight.isPressed
    }
    fun isMovingUp(): Boolean {
        return listenerUp.isPressed
    }
    fun isMovingDown(): Boolean {
        return listenerDown.isPressed
    }

    fun isIdle(): Boolean {
        return !(listenerDown.isPressed || listenerLeft.isPressed || listenerRight.isPressed || listenerUp.isPressed)
    }

    init {

        // dpadTable.debug = true

        val buttonLeftTexture = Texture(Gdx.files.internal("controller/left.png"))
        val buttonRightTexture = Texture(Gdx.files.internal("controller/right.png"))
        val buttonUpTexture = Texture(Gdx.files.internal("controller/up.png"))
        val buttonDownTexture = Texture(Gdx.files.internal("controller/down.png"))

        val buttonRight = ImageButton(TextureRegionDrawable(TextureRegion(buttonRightTexture)))
        val buttonLeft = ImageButton(TextureRegionDrawable(TextureRegion(buttonLeftTexture)))
        val buttonUp = ImageButton(TextureRegionDrawable(TextureRegion(buttonUpTexture)))
        val buttonDown = ImageButton(TextureRegionDrawable(TextureRegion(buttonDownTexture)))

        buttonLeft.addListener(listenerLeft)
        buttonRight.addListener(listenerRight)
        buttonUp.addListener(listenerUp)
        buttonDown.addListener(listenerDown)

        dpadTable.left().bottom()
        dpadTable.add().width(160f).height(160f)
        dpadTable.add(buttonUp).width(160f).height(160f)
        dpadTable.add().width(160f).height(160f)
        dpadTable.row()
        dpadTable.add(buttonLeft).width(160f).height(160f)
        dpadTable.add().width(160f).height(160f)
        dpadTable.add(buttonRight).width(160f).height(160f)
        dpadTable.row()
        dpadTable.add().width(150f).height(160f)
        dpadTable.add(buttonDown).width(160f).height(160f)
        dpadTable.add().width(160f).height(160f)

        stage.addActor(dpadTable)
    }
}