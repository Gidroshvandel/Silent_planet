package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.CellProperties
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.utils.getPlayerByName
import com.silentgames.silent_planet.utils.getSpaceShip


/**
 * Created by gidroshvandel on 21.06.17.
 */
class MainPresenter internal constructor(
        private val view: MainContract.View,
        private val viewModel: MainViewModel,
        private val model: MainModel
) : MainContract.Presenter {

    private val isClickForCurrentPosition: Boolean
        get() = viewModel.gameMatrixHelper.oldXY != null
                && viewModel.gameMatrixHelper.currentXY == viewModel.gameMatrixHelper.oldXY

    override fun onSingleTapConfirmed(x: Int, y: Int) {
        select(Axis(x, y))
    }

    override fun onCellListItemSelectedClick(x: Int, y: Int, text: String) {
        view.showToast(App.getContext().resources.getString(R.string.selectPlayer) + " " + text)
        selectEntity(Axis(x, y), text)
    }

    override fun onActionButtonClick() {
        viewModel.gameMatrixHelper = getCrystal(viewModel.gameMatrixHelper)
        if (!overZeroCrystals()) {
            view.enableButton(false)
        }
        if (viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType.getSpaceShip() != null) {
            view.setImageCrystalText(
                    viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType.getSpaceShip()!!.crystals.toString())
        } else {
            if (viewModel.gameMatrixHelper.playerName != null)
                view.setImageCrystalText(
                        viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType.getPlayerByName(viewModel.gameMatrixHelper.playerName!!)!!.crystals.toString())
        }
    }

    override fun onEntityDialogElementSelect(entityType: CellProperties) {
        if (entityType is EntityType) {
            selectEntity(entityType)
        } else if (entityType is CellType) {
            selectCell(entityType)
        }
    }

    override fun onCreate() {
        val gameMatrixHelper = GameMatrixHelper(model.fillBattleGround())
        gameMatrixHelper.isEventMove = false
        viewModel.gameMatrixHelper = gameMatrixHelper

        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
        view.enableButton(false)

    }

    private fun select(currentXY: Axis) {
        viewModel.gameMatrixHelper.currentXY = currentXY

        val oldXY = viewModel.gameMatrixHelper.oldXY
        val entityType = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType
        val cellType = viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType

        if (viewModel.gameMatrixHelper.selectedEntity == null &&
                (oldXY == null || isClickForCurrentPosition)) {
            if (entityType.isNotEmpty()) {
                view.showEntityMenuDialog(entityType, cellType)
            } else {
                selectCell()
            }
        } else {
            tryMove(viewModel.gameMatrixHelper.selectedEntity!!)
        }
    }

    private fun selectEntity(entityType: EntityType) {
        view.setImageCrystalText(entityType.crystals.toString())
        if (overZeroCrystals()) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        view.showObjectIcon(entityType)
        viewModel.gameMatrixHelper.oldXY = viewModel.gameMatrixHelper.currentXY
        viewModel.gameMatrixHelper.selectedEntity = entityType
    }

    private fun selectCell(cellType: CellType) {
        view.enableButton(false)
        if (cellType.isVisible) {
            view.setImageCrystalText(cellType.crystals.toString())
        }
        view.showObjectIcon(viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType)
        view.hideCellListItem()
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.selectedEntity = null
        viewModel.gameMatrixHelper.playerName = null
    }

//    private fun select(currentXY: Axis, oldXY: Axis?, name: String?) {
//        viewModel.gameMatrixHelper.currentXY = currentXY
//
//        val entityType = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType
//
//        if (oldXY == null || isClickForCurrentPosition) {
//            if (entityType.isNotEmpty() && !viewModel.isDoubleClick) {
//                selectEntity(currentXY, name)
//            } else {
//                selectCell()
//            }
//        } else {
//            tryMove()
//        }
//    }

    private fun overZeroCrystals(): Boolean {
        return viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType.crystals > 0
    }

    private fun selectEntity(currentXY: Axis, name: String?) {
        val en = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType
        if (en.getSpaceShip() != null) {
            view.setImageCrystalText(en.getSpaceShip()!!.crystals.toString())
        }
        if (name == null) {
            view.showCellListItem(currentXY.x, currentXY.y, model.getPlayersNameOnCell(viewModel.gameMatrixHelper.gameMatrixCellByXY))
        }
        if (name != null) {
            viewModel.gameMatrixHelper.playerName = name
            if (en.getSpaceShip() == null) {
                view.setImageCrystalText(en.getPlayerByName(name)!!.crystals.toString())
            }
        }
        if (overZeroCrystals()) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType.let { view.showObjectIcon(it.first()) }
        viewModel.gameMatrixHelper.oldXY = currentXY
    }

    private fun selectCell() {
        view.enableButton(false)
        val cellType = viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType
        if (cellType.isVisible) {
            view.setImageCrystalText(cellType.crystals.toString())
        }
        view.showObjectIcon(viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType)
        view.hideCellListItem()
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.selectedEntity = null
        viewModel.gameMatrixHelper.playerName = null
    }

    private fun tryMove(entity: EntityType) {
        view.enableButton(false)
        val newGameMatrix = EntityMove(viewModel.gameMatrixHelper).canMove(entity)
        if (newGameMatrix != null) {
            viewModel.gameMatrixHelper = newGameMatrix
            doEvent()
            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
            view.showToast(App.getContext().resources.getString(R.string.turnMessage) + " " + TurnHandler.fractionType.toString())
        }
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.selectedEntity = null
        viewModel.gameMatrixHelper.playerName = null
    }

    private fun checkToWin() {

    }

    private fun doEvent() {
        var count = 0
        while (viewModel.gameMatrixHelper.isEventMove) {
            viewModel.gameMatrixHelper.isEventMove = false
            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
            viewModel.gameMatrixHelper = EntityMove(viewModel.gameMatrixHelper).doEvent()
            count++
            if (count > 20) {
                break
            }
        }
    }

    private fun getCrystal(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        val cellType = gameMatrixHelper.gameMatrixCellByXY.cellType

        if (cellType.crystals > 0) {
            gameMatrixHelper.selectedEntity?.apply { crystals++ }
            cellType.apply { crystals-- }
        }
        return gameMatrixHelper
    }
}
