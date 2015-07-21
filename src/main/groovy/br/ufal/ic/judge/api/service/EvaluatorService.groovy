package br.ufal.ic.judge.api.service

import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.service.receiver.Receiver
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import org.apache.commons.logging.LogFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.ConnectionFactory
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

    static def logger = LogFactory.getLog(EvaluatorService)

    Queue queue = new Queue("judge-api-evaluator", true)

    @Autowired
    RabbitTemplate rabbitTemplate

    @Bean
    Queue queue() {
        return queue
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter evaluatorMessageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setQueueNames(queue.name)
        container.setMessageListener(evaluatorMessageListenerAdapter)
        return container
    }

    @Bean
    Receiver receiver() {
        new Receiver()
    }

    @Bean
    MessageListenerAdapter evaluatorMessageListenerAdapter(Receiver receiver) {
        new MessageListenerAdapter(receiver, "receiveEvaluatorMessage")
    }


    @PostConstruct
    def checkSubmissionsQueue() {
        receiver().getLatch().await(10000, TimeUnit.MILLISECONDS)
    }

    def evaluate(Submission submission) {

        def json = new String(new ObjectMapper().writeValueAsBytes(submission), "UTF-8")

        if (logger.debugEnabled) {
            logger.debug "Enviando a submissão #$submission.id para avaliação. \n ${JsonOutput.prettyPrint(json)}"
        }

        rabbitTemplate.convertAndSend(
                "EXCHANGE",
                "evaluator", new Message(json.bytes,
                new MessageProperties(replyTo: queue.name))
        )
    }
}