package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getName
import java.io.Serializable

class GameState(
        cellList: List<CellEcs>,
        unitList: List<UnitEcs>,
        firstTurnFraction: FractionsType
) : Serializable {

    val turn: Turn = Turn(firstTurnFraction)

    val cellMap: List<CellEcs> get() = mutableCellList.toList()
    val unitMap: List<UnitEcs> get() = mutableUnitList.toList()

    private val mutableCellList: MutableList<CellEcs> = cellList.toMutableList()
    private val mutableUnitList: MutableList<UnitEcs> = unitList.toMutableList()

    fun getUnit(id: Long) = unitMap.find { it.id == id }

    fun getCell(id: Long) = unitMap.find { it.id == id }

    fun getCell(axis: Axis) = cellMap.getByPosition(axis)

    fun getUnit(axis: Axis) = unitMap.getByPosition(axis)

    fun getUnits(axis: Axis) =
            unitMap.filter { it.getComponent<Position>()?.currentPosition == axis }

    fun getCapitalShip(unitFractionsType: FractionsType) =
            unitMap.findCapitalShip(unitFractionsType)

    fun getCapitalShipPosition(unitFractionsType: FractionsType) =
            getCapitalShip(unitFractionsType)?.getComponent<Position>()

    fun getAllFractionUnits(fractionsType: FractionsType): List<UnitEcs> =
            unitMap.filter { it.getComponent<FractionsType>() == fractionsType }

    fun moveUnit(unit: UnitEcs, toPosition: Axis) {
        if (!unitMap.contains(unit)) {
            mutableUnitList.add(unit)
        }
        CoreLogger.logDebug("Moving", "unit ${unit.getName()} start Moving")
        unit.addComponent(MovedSuccess())
        unit.getComponent<Position>()?.currentPosition = toPosition
    }

    fun removeUnit(unit: UnitEcs) {
        mutableUnitList.remove(unit)
    }

    fun makeCurrentFractionTurnUnitsCanTurn() {
        makeUnitsCanTurn(turn.currentTurnFraction)
    }

    fun isTurnEnd() =
            getAllFractionUnits(turn.currentTurnFraction).find { !it.hasComponent<CanTurn>() } != null

    private fun makeUnitsCanTurn(fractionsType: FractionsType) {
        unitMap.filter {
            it.getComponent<FractionsType>() == fractionsType
        }.forEach {
            it.addComponent(CanTurn())
        }
    }

    private fun <T : EntityEcs> List<T>.getByPosition(axis: Axis) =
            find { it.getComponent<Position>()?.currentPosition == axis }

    private fun List<UnitEcs>.findCapitalShip(unitFractionsType: FractionsType) =
            find { it.hasComponent<CapitalShip>() && it.getComponent<FractionsType>() == unitFractionsType }

}