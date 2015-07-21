package br.ufal.ic.judge.api.service

import br.ufal.ic.judge.api.domain.User
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import org.apache.commons.logging.LogFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmailService {

    static def logger = LogFactory.getLog(EvaluatorService)

    @Autowired
    RabbitTemplate rabbitTemplate

    def sendMail(User user, String subject, String body) {

        def json = new String(new ObjectMapper().writeValueAsBytes([
                to: user.email,
                subject: subject,
                body: body
        ]), "UTF-8")

        if (logger.debugEnabled) {
            logger.debug "Enviando email para $user.email. \n ${JsonOutput.prettyPrint(json)}"
        }

        rabbitTemplate.convertAndSend(
                "EXCHANGE",
                "email", json.bytes
        )
    }

}
