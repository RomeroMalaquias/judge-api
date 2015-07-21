package br.ufal.ic.judge.api.rabbitmq

import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.repository.SubmissionRepository
import groovy.json.JsonSlurper
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.concurrent.CountDownLatch

@Component
class Receiver {


    static def logger = LogFactory.getLog(Receiver)

    @Autowired
    SubmissionRepository submissionRepository

    CountDownLatch latch = new CountDownLatch(1);

    def receiveMessage(byte[] message) {
        try {

            if (logger.debugEnabled) {
                logger.debug "Recebendo a mensage: ${new String(message, 'UTF-8')}"
            }

            def json = new JsonSlurper().parseText(new String(message, 'UTF-8'))

            if (json["id"] && json["__result"]) {

                Long id = json["id"] as Long
                Submission.Status status = Submission.Status.valueOf(json["__result"] as String)

                if (logger.infoEnabled) {
                    logger.info "Resultado $status da submissão $id recebido."
                }

                if (logger.debugEnabled) {
                    logger.debug json
                }

                def submission = submissionRepository.findOne(id)

                if (submission) {
                    submission.status = status
                    submissionRepository.save(submission)
                }
            }
        } catch (e) {
            e.printStackTrace()
        }

        latch.countDown()
    }

}
