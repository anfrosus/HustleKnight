package com.woozy.untitled.infra.sse

import com.woozy.untitled.dto.BattleDto
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Controller
class SseController {

    private val emitterList = ConcurrentHashMap<Long, SseEmitter>()

    @GetMapping("/sse/{playerId}")
    fun connect(@PathVariable playerId: Long): SseEmitter{
        val emitter = SseEmitter(5 * 60 * 1000)
        this.emitterList[playerId] = emitter
        emitter.onTimeout { this.emitterList.remove(playerId) }
        emitter.send("Success")

        return emitter
    }

    fun pushBattleResult(playerId: Long, battleResult: BattleDto){
        println("아니 이거 타긴 타?")
        this.emitterList[playerId]?: println("널이다 이누마")
        this.emitterList[playerId]?.let { emitter ->
            try {
                println("샌드 직전!!!")
                emitter.send(battleResult)
                println(emitter.toString())
                emitter.complete()
                this.emitterList.remove(playerId)
            } catch (e: Exception) {
                //TODO: NullPointer 따로 잡자
                println(e.message)
                emitter.completeWithError(e)
            }
        }
    }

}