package com.woozy.untitled.dto.response

import com.woozy.untitled.model.Player
import com.woozy.untitled.model.enums.PlayerRole

data class PlayerResponseDto(
    val id: Long,
    val email: String,
    val role: PlayerRole,
    val name: String,
    val level: Int,
    val atkDmg: Long,
    val atkSpd: Long,
    val hitPnt: Long,
    val addiAtkDmg: Long,
    val addiAtkSpd: Long,
    val addiHitPnt: Long,
    val maxStage: Long,
    val curStage: Long,
    val exp: Long,
    val gold: Long,
    val ticket: Int,
    val raidScore: Long
) {
    companion object {
        fun fromEntity(player: Player): PlayerResponseDto {
            return PlayerResponseDto(
                id = player.id!!,
                email = player.email,
                role = player.role,
                name = player.name,
                level = player.level,
                atkDmg = player.atkDmg,
                atkSpd = player.atkSpd,
                hitPnt = player.hitPnt,
                addiAtkDmg = player.addiAtkDmg,
                addiAtkSpd = player.addiAtkSpd,
                addiHitPnt = player.addiHitPnt,
                maxStage = player.maxStage,
                curStage = player.curStage,
                exp = player.exp,
                gold = player.gold,
                ticket = player.ticket,
                raidScore = player.raidScore
            )
        }
    }
}