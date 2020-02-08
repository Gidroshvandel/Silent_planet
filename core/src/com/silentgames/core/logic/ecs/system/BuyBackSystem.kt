package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.event.BuyBackEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class BuyBackSystem(
        private val onSuccess: (name: String) -> Unit,
        private val onFailure: (missingAmount: Int) -> Unit
) : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "BuyBackSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<BuyBackEvent>()?.let {
            notNull(
                    unit.getComponent(),
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::buyBack
            )
            unit.removeComponent(it)
        }
    }

    private fun buyBack(
            capture: Capture,
            unitFractionsType: FractionsType,
            unit: UnitEcs,
            gameState: GameState
    ) {
        val unitCapitalShip = gameState.getCapitalShip(unitFractionsType)
        val unitFractionCrystals = unitCapitalShip?.getComponent<CrystalBag>()
        val invadersCapitalShip = gameState.getCapitalShip(capture.invaderFaction)
        val invadersFractionCrystals = invadersCapitalShip?.getComponent<CrystalBag>()
        if (invadersFractionCrystals != null
                && unitFractionCrystals != null
                && invadersFractionCrystals.addCrystals(unitFractionCrystals, capture.buybackPrice)
        ) {
            unit.removeComponent(capture)
            unit.addComponent(Teleport())
            unit.addComponent(Active())
            unitCapitalShip.getComponent<Position>()?.currentPosition?.let { unit.addComponent(TargetPosition(it)) }
            onSuccess.invoke(unit.getComponent<Description>()?.name ?: "")
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} buyBack success")
        } else {
            onFailure.invoke(capture.buybackPrice.minus(unitFractionCrystals?.amount ?: 0))
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} buyBack failure")
        }
    }

}