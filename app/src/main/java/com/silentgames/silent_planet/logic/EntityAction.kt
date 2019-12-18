package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.*
import com.silentgames.silent_planet.model.effects.CaptureEffect
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.ShipNotFoundException
import com.silentgames.silent_planet.utils.getSpaceShip
import com.silentgames.silent_planet.utils.isSpaceShip
import com.silentgames.silent_planet.utils.isSpaceShipBelongFraction

fun GameMatrix.tryMoveEntity(target: Axis, entity: EntityType): Boolean {
    val entityPosition = this.getEntityCell(entity)?.cellType?.position
    return if (entityPosition != null && targetIsAvailable(target, entityPosition)) {
        when (entity) {
            is SpaceShip -> tryMoveShip(target, entity)
            is Player -> tryMovePlayer(target, entity)
            else -> false
        }
    } else {
        false
    }
}

fun GameMatrix.tryMoveShip(target: Axis, spaceShip: SpaceShip): Boolean {
    val targetCell = this.getCell(target)
    return if (spaceShip.isCanFly
            && targetCell.cellType.isCanFly
            && !this.isSpaceShip(target)) {
        moveShip(target, spaceShip)
        true
    } else {
        false
    }
}

private fun GameMatrix.moveShip(target: Axis, spaceShip: SpaceShip) {
    val targetCell = this.getCell(target)
    val currentCell = this.getEntityCell(spaceShip)
    targetCell.entityType.add(spaceShip)
    targetCell.cellType.isVisible = true
    currentCell?.entityType?.remove(spaceShip)
}

fun GameMatrix.targetIsAvailable(target: Axis, current: Axis) =
        isMoveAtDistance(target, current)
                && this.isNowPlaying(current, TurnHandler.fractionType)

private fun isMoveAtDistance(target: Axis, current: Axis): Boolean {
    if (!(current.x == target.x && current.y == target.y)) {
        for (i in 0..2) {
            for (j in 0..2) {
                if (current.x + j - 1 == target.x && current.y + i - 1 == target.y) {
                    return true
                }
            }
        }
    }
    return false
}

private fun GameMatrix.isNowPlaying(axis: Axis, fractionsType: FractionsType): Boolean {
    return this.getCell(axis).entityType.firstOrNull {
        it.fraction.fractionsType == fractionsType
    } != null
}

fun GameMatrix.tryMovePlayer(target: Axis, player: Player): Boolean {
    val targetCell = this.getCell(target)
    return if (targetCell.cellType.isCanMove && player.isCanMove) {
        val enemy = this.getEnemy(target, TurnHandler.fractionType)
        if (enemy != null) {
            this.captureEnemyUnit(player, enemy)
            true
        } else {
            this.movePlayer(target, player)
            true
        }
    } else if (isSpaceShip(target) && targetCell.entityType.isSpaceShipBelongFraction(player)) {
        this.moveOnBoardAllyShip(player)
        true
    } else {
        false
    }
}

private fun GameMatrix.isSpaceShip(axis: Axis): Boolean {
    return this.getCell(axis).entityType.isSpaceShip()
}

fun GameMatrix.movePlayer(target: Axis, player: Player) {
    val targetCell = this.getCell(target)
    val playerCellPosition = this.getEntityCell(player)?.cellType?.position
    targetCell.entityType.add(player)
    targetCell.cellType.isVisible = true
    playerCellPosition?.let { deletePlayer(it, player) }
}

fun GameMatrix.buyBack(
        player: Player,
        onSuccess: () -> Unit,
        onFailure: (missingAmount: Int) -> Unit
) {
    val playerShip = getShip(player.fraction.fractionsType)
    val captureEffect = player.getEffect<CaptureEffect>()
    if (playerShip.crystals >= captureEffect?.buybackPrice ?: 0) {
        playerShip.crystals = playerShip.crystals - (captureEffect?.buybackPrice ?: 0)
        captureEffect?.remove()
        moveOnBoard(player, playerShip)
        onSuccess.invoke()
    } else {
        onFailure.invoke(captureEffect?.buybackPrice?.minus(playerShip.crystals) ?: 0)
    }
}

fun GameMatrix.captureEnemyUnit(player: Player, enemyPlayer: Player) {
    moveOnBoardAllyShip(player)
    CaptureEffect(enemyPlayer, player.fraction.fractionsType)
    moveOnBoardFractionShip(enemyPlayer, player.fraction.fractionsType)
}

fun GameMatrix.getEnemy(axis: Axis, currentFraction: FractionsType): Player? {
    return getCell(axis).entityType.getEnemy(currentFraction)
}

private fun MutableList<EntityType>.getEnemy(currentFraction: FractionsType) =
        find { !it.isDead && it.fraction.fractionsType != currentFraction && it is Player } as? Player

private fun GameMatrix.moveOnBoard(player: Player, spaceShip: SpaceShip) {
    val playerCellPosition = this.getEntityCell(player)?.cellType?.position
    spaceShip.crystals = spaceShip.crystals + player.crystals
    spaceShip.playersOnBord.add(player.apply { crystals = 0 })
    playerCellPosition?.let { deletePlayer(it, player) }
}

fun GameMatrix.moveOnBoardAllyShip(player: Player) {
    moveOnBoard(player, findFractionShip(player.fraction.fractionsType))
}

private fun GameMatrix.moveOnBoardFractionShip(player: Player, fractionsType: FractionsType) {
    moveOnBoard(player, findFractionShip(fractionsType))
}

private fun GameMatrix.findFractionShip(fractionsType: FractionsType): SpaceShip {
    var spaceShip: SpaceShip? = null
    this.forEach { arrayOfCells ->
        val y = arrayOfCells.indexOfFirst {
            spaceShip = it.entityType.getSpaceShip()
            if (spaceShip?.fraction?.fractionsType == fractionsType) {
                true
            } else {
                spaceShip = null
                false
            }
        }
        spaceShip?.let {
            if (y != -1) {
                return it
            }
        }
    }
    throw ShipNotFoundException()
}

private fun GameMatrix.deletePlayer(position: Axis, player: Player) {
    this.getCell(position).entityType.deletePlayer(player)
}

private fun MutableList<EntityType>.deletePlayer(player: Player) {
    if (!remove(player)) {
        getSpaceShip()?.playersOnBord?.remove(player)
    }
}