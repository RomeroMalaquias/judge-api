package br.ufal.ic.judge.api.service

import br.ufal.ic.judge.api.repository.SimilarityRepository
import br.ufal.ic.judge.api.repository.SubmissionRepository
import br.ufal.ic.judge.api.service.receiver.Receiver
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import org.apache.commons.logging.LogFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit

@Service
class SimilarityService {

    static def logger = LogFactory.getLog(EvaluatorService)

    Queue queue = new Queue("judge-api-similarity", true)

    @Autowired
    RabbitTemplate rabbitTemplate

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    SimilarityRepository similarityRepository

    @Bean
    Queue similarityQueue() {
        return queue
    }

    @Bean
    SimpleMessageListenerContainer similarityContainer(ConnectionFactory connectionFactory, MessageListenerAdapter similarityMessageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setQueueNames(queue.name)
        container.setMessageListener(similarityMessageListenerAdapter)
        return container
    }

    @Bean
    Receiver receiver() {
        new Receiver()
    }

    @Bean
    MessageListenerAdapter similarityMessageListenerAdapter(Receiver receiver) {
        new MessageListenerAdapter(receiver, "receiveSimilarityMessage")
    }


    @PostConstruct
    def checkSubmissionsQueue() {
        receiver().getLatch().await(10000, TimeUnit.MILLISECONDS)
    }

    def update() {

        def submissions = submissionRepository.findAll()

        def json = new String(new ObjectMapper().writeValueAsBytes(submissions), "UTF-8")

        if (logger.debugEnabled) {
            logger.debug "Atualizando as similaridades. \n ${JsonOutput.prettyPrint(json)}"
        }

        rabbitTemplate.convertAndSend(
                "EXCHANGE",
                "similarity", new Message(json.bytes,
                new MessageProperties(replyTo: queue.name))
        )
    }

}
