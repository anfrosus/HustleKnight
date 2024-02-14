package com.woozy.untitled.unit.fixture

import com.woozy.untitled.model.GoodsDropTable
import com.woozy.untitled.model.Monster
import com.woozy.untitled.model.RaidMonster
import com.woozy.untitled.model.enums.MonsterType
import io.mockk.mockk

class MonsterFixture {

    companion object {
        fun getMonsterBase(): Monster {
            val goodsDropTable: GoodsDropTable = mockk()
            return Monster(MonsterType.NORMAL, 1, "테스트몬스터", 1, 1, 1, 10, 10, goodsDropTable = goodsDropTable)

        }
        fun getRaidMonsterBase(): RaidMonster {
            return RaidMonster("레이드몬스터", 1000, 10)
        }
    }
}