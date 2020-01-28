package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType.TELEPORT
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class AntiloopSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<FractionsType>()?.let {
            notNull(unit.getComponent<Route>()?.paths,
                    unit,
                    gameState.getCapitalShipPosition(it)?.currentPosition,
                    ::breakCycle)
        }
    }

    private fun breakCycle(paths: MutableList<Motion>, unit: UnitEcs, shipPosition: Axis) {
        val marker = this.markerCycleMotion(2, paths)
        val last = paths.lastOrNull()
        if (this.isTeleport(paths)) {
            if (last?.axis == marker?.axis) {
                unit.addComponent(Teleport())
                unit.addComponent(TargetPosition(shipPosition))
            }
        }
    }

    private fun markerCycleMotion(reduceIndex: Int, paths: MutableList<Motion>): Motion? {
        val index = paths.lastIndex - reduceIndex
        val isNormalIndex = (index in 0..paths.size)
        return if (isNormalIndex) paths[index] else null
    }

    private fun isTeleport(paths: MutableList<Motion>): Boolean {
        return (this.markerCycleMotion(1, paths)?.motionType == TELEPORT
                && this.markerCycleMotion(2, paths)?.motionType == TELEPORT
                && paths.lastOrNull()?.motionType == TELEPORT)
    }
}