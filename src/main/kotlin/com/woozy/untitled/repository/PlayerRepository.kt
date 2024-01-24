package com.woozy.untitled.repository

import com.woozy.untitled.model.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository: JpaRepository<Player, Long> {

}
