package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "TurnSystem"
    }

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        engine.gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<Moving>()) {
            gameState.finishTurn(unit)
        }
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)
        if (gameState.isTurnEnd() && gameState.isMovingFinish()) {
            gameState.turn.nextTurn()
            gameState.makeCurrentFractionTurnUnitsCanTurn()
            gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
            CoreLogger.logDebug(SYSTEM_TAG, gameState.turn.currentTurnFraction.name)
        }
    }

    private fun GameState.makeCurrentFractionTurnUnitsCanTurn() {
        makeUnitsCanTurn(turn.currentTurnFraction)
    }

    private fun GameState.makeUnitsCanTurn(fractionsType: FractionsType) {
        unitMap.filter {
            it.getComponent<FractionsType>() == fractionsType
                    && it.hasComponent<Active>()
                    && it.hasComponent<TurnMode>()
        }.forEach {
            it.addComponent(CanTurn())
        }
    }

}

fun GameState.finishTurn(unit: UnitEcs) {
    val turnMode = unit.getComponent<TurnMode>()
    if (turnMode != null) {
        unitMap.filter {
            it.getComponent<TurnMode>()?.groupType == turnMode.groupType
        }.forEach {
            it.removeComponent(CanTurn::class.java)
        }
    }
}

fun GameState.finishTurn() {
    unitMap.forEach { unit ->
        unit.removeComponent(CanTurn::class.java)
    }
}