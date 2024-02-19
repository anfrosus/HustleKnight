package com.woozy.untitled.infra.redis

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.woozy.untitled.dto.BattleInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun battleRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<Long, BattleInfo> {
        val template = RedisTemplate<Long, BattleInfo>()

        template.connectionFactory = connectionFactory
        //트랜잭션 지원 활성화
//        template.setEnableTransactionSupport(true)

        // 직렬화/역직렬화 설정. 기본적으로 JDK의 직렬화 사용
        //template.keySerializer = StringRedisSerializer()
        //template.valueSerializer = JdkSerializationRedisSerializer()
        template.keySerializer = GenericToStringSerializer(Long::class.java)
        template.valueSerializer = Jackson2JsonRedisSerializer(jacksonObjectMapper(), BattleInfo::class.java)

        return template
    }

    @Bean
    fun emitterRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, Boolean>{
        val template = RedisTemplate<String, Boolean>()
        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericToStringSerializer(Boolean::class.java)

        return template
    }
}