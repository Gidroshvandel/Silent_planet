package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Scaling
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.AppViewport


class SilentPlanetGame : ApplicationAdapter(), SilentPlanetContract.View {

    private lateinit var presenter: SilentPlanetContract.Presenter

    private val viewPort = AppViewport(Scaling.fillY, WIDTH, HEIGHT)
    private val camera by lazy(viewPort::getCamera)

    private val hud by lazy { Hud() }

    override fun create() {

        presenter = SilentPlanetPresenter(
                this,
                null,
                SilentPlanetViewModel(),
                SilentPlanetModel(viewPort)
        )

        initUi()

        presenter.onCreate()
    }

    private fun initUi() {
        AssetManager().load("ui/uiskin.json", TextureAtlas::class.java)
        (camera as? OrthographicCamera)?.zoom = 1f
        (camera as? OrthographicCamera)?.setToOrtho(true)
        camera.position.x = WIDTH / 2f
        camera.position.y = HEIGHT / 2f
    }

    override fun render() {
        Gdx.gl.glClearColor(0.5f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        presenter.onRender()


        hud.stage.viewport.apply(true)

        hud.stage.act()
        hud.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewPort.update(width, height)


        hud.stage.viewport.update(width, height)
    }

    override fun dispose() {
//        engine.dispose()
    }

    companion object {
        const val WIDTH = 640f
        const val HEIGHT = 640f
    }

    override fun showToast(text: String) {
    }

    override fun fillEntityName(text: String) {
    }

    override fun fillDescription(text: String) {
    }

    override fun showObjectIcon(bitmap: String) {
        hud.updateImage(bitmap)
    }

    override fun enableButton(isEnabled: Boolean) {
    }

    override fun setImageCrystalText(text: String) {
    }

    override fun showEntityMenuDialog(entityList: MutableList<EntityData>, currentCell: EntityData) {
        hud.update(entityList) {
            presenter.onEntityDialogElementSelect(it)
        }
    }

    override fun changeAlienCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.ALIEN, crystals)
    }

    override fun changeHumanCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.HUMAN, crystals)
    }

    override fun changePirateCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.PIRATE, crystals)
    }

    override fun changeRobotCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.ROBOT, crystals)
    }

    override fun selectCurrentFraction(fractionType: FractionsType) {
        hud.selectFraction(fractionType)
    }

    override fun showPlayerBuybackSuccessMessage(name: String) {
    }

    override fun showPlayerBuybackFailureMessage(missingAmount: Int) {
    }
}