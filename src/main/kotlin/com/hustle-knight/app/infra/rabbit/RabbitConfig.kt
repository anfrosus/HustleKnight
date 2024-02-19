package com.woozy.untitled.infra.rabbit

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig(
) {
    private val delayedExchangeName = "battle-delayed-exchange"
    private val routingKey = "battle-routing-key"

    @Bean
    fun delayedExchange(): Exchange {
        val args: MutableMap<String, Any?> = HashMap()
        args["x-delayed-type"] = "direct"
        return CustomExchange(delayedExchangeName, "x-delayed-message", true, false, args)
    }

    @Bean
    fun createQueue(): Queue {
        return Queue("test-queue")
    }

    @Bean
    fun binding(delayedExchange: Exchange): Binding {
        return BindingBuilder.bind(Queue("test-queue"))
            .to(delayedExchange)
            .with(routingKey)
            .noargs()
    }



//    @Bean
//    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
//        val rabbitTemplate = RabbitTemplate(connectionFactory)
//        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
//        return rabbitTemplate
//    }
}