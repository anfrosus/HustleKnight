package com.woozy.untitled.infra.security

import com.woozy.untitled.model.enums.PlayerRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val authorities: MutableCollection<GrantedAuthority> = mutableSetOf()
){
    constructor(id: Long, role: String) : this(id){
        authorities.add(
            SimpleGrantedAuthority("ROLE_$role")
        )
    }

}