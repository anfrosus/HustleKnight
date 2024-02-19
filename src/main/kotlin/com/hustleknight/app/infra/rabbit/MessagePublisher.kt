package com.hustleknight.app.infra.rabbit

import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.stereotype.Service

@Service
class MessagePublisher(private val rabbitTemplate: RabbitTemplate) {

    private val delayedExchangeName = "battle-delayed-exchange"
    private val routingKey = "battle-routing-key"



    fun sendDelayedMessage(delay: Long, battleId: Long) {
//        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        rabbitTemplate.convertAndSend(delayedExchangeName, routingKey, battleId,
            MessagePostProcessor { delayedMessage ->
            delayedMessage.messageProperties.setHeader("x-delay", delay)
            delayedMessage
        })
    }

    fun sendMessage(exchange: String, routingKey: String, message: Any) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message)
    }
}