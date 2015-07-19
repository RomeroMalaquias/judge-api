package br.ufal.ic.judge.api.service

import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.rabbitmq.Receiver
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.UniquelyNamedQueue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.amqp.core.Queue

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit

@Service
class EvaluatorService {

    Queue queue = new UniquelyNamedQueue()

    @Autowired
    RabbitTemplate rabbitTemplate

    @Bean
    Queue queue() {
        return queue
    }

//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("spring-boot-exchange")
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME)
//    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setQueueNames(queue.name)
        container.setMessageListener(messageListenerAdapter)
        return container
    }

    @Bean
    Receiver receiver() {
        new Receiver()
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(Receiver receiver) {
        new MessageListenerAdapter(receiver, "receiveMessage")
    }


    @PostConstruct
    def checkSubmissionsQueue() {
        sendMessages()
        receiver().getLatch().await(10000, TimeUnit.MILLISECONDS)
    }

    def sendMessages () {
        Thread.start {
            while (true) {
                println "Waiting five seconds..."
                Thread.sleep(5000)
                println "Sending message..."
                rabbitTemplate.convertAndSend(queue.name, "Hello from RabbitMQ")
            }
        }
    }

    def evaluate(Submission submission) {
        rabbitTemplate.convertAndSend(queue.name, new ObjectMapper().writeValueAsString(submission))
    }

}
