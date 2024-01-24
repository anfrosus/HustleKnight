package com.woozy.untitled.model

import jakarta.persistence.*

@Entity
@Table(name = "EQUIPPED")
class Equipped(
    @Id
    var id: Long,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    val player: Player,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPPED_WEAPON")
    var weapon: InstancedItem,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPPED_ACCESSORY")
    var accessory: InstancedItem,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPPED_ARMOR")
    var armor: InstancedItem,

) {

}