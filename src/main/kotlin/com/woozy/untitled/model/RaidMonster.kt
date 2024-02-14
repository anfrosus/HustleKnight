package com.woozy.untitled.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import jakarta.persistence.*

@Entity
@Table(name = "RAID_MONSTER")
class RaidMonster(
    @Column(name = "RAID_MONSTER_NAME")
    var name: String,

    @Column(name = "RAID_MONSTER_HP")
    var totalHp: Long,

    @Column(name = "RAID_MONSTER_RESISTANCE")
    var resistance: Long

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun underAttack(player: Player): Long{
        if(totalHp <= 0) {
            throw CustomException(ErrorCode.MONSTER_HP_ZERO)
        }
        val damage = (player.atkDmg + player.addiAtkDmg) / resistance
        val totalDamage = damage * ((player.atkSpd / 60) + 1)
        val prevHp = totalHp
        if (totalHp <= totalDamage) {
            totalHp = 0
        } else {
            totalHp -= totalDamage
        }
        val score = prevHp - totalHp
        return score
    }
}