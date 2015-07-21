package br.ufal.ic.judge.api.service.receiver

import br.ufal.ic.judge.api.domain.Similarity
import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.repository.SimilarityRepository
import br.ufal.ic.judge.api.repository.SubmissionRepository
import br.ufal.ic.judge.api.service.EmailService
import groovy.json.JsonOutput
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

    @Autowired
    SimilarityRepository similarityRepository

    @Autowired
    EmailService emailService

    CountDownLatch latch = new CountDownLatch(1);

    def receiveEvaluatorMessage(byte[] message) {
        try {

            def jsonString = new String(message, 'UTF-8')

            if (logger.debugEnabled) {
                logger.debug "Recebendo a mensagem do avaliador:\n${JsonOutput.prettyPrint(jsonString)}"
            }

            def json = new JsonSlurper().parseText(jsonString)

            if (json["id"] && json["__result"]) {

                Long id = json["id"] as Long
                Submission.Status status = Submission.Status.valueOf(json["__result"] as String)

                if (logger.infoEnabled) {
                    logger.info "Resultado $status da submissão #$id recebido."
                }

                if (logger.debugEnabled) {
                    logger.debug JsonOutput.prettyPrint(jsonString)
                }

                def submission = submissionRepository.findOne(id)

                if (submission) {
                    submission.status = status

                    def subject = "The Huxley Online Judge - Seu código foi avaliado"
                    def body = "Olá $submission.user.name, \n\nO seu código acabou de ser avaliado com o " +
                            "status: $submission.status. \n\nAtenciosamente, \nThe Huxley Online Judge"


                    emailService.sendMail(submission.user, subject, body)


                    submissionRepository.save(submission)
                }
            }
        } catch (e) {
            e.printStackTrace()
        }

        latch.countDown()
    }

    def receiveSimilarityMessage(byte[] message) {
        try {

            def jsonString = new String(message, 'UTF-8')

            if (logger.debugEnabled) {
                logger.debug "Recebendo a mensagem de similaridade:\n${JsonOutput.prettyPrint(jsonString)}"
            }

            def json = new JsonSlurper().parseText(jsonString)

            json.each { submissionJson ->
                if (submissionJson['id'] && submissionJson['__similarities']) {
                    def submissionId = submissionJson['id'] as Long

                    if (submissionId) {
                        Submission submission = submissionRepository.findOne(submissionId)

                        if (submission) {
                            List<Similarity> similarities = []

                            submissionJson['__similarities'].each { similarJson ->
                                if(similarJson['id'] && similarJson['__rate']) {
                                    def similarId = similarJson['id'] as Long
                                    def rate = similarJson['__rate'] as Double

                                    if (similarId && rate > 0.6) {
                                        def similar = submissionRepository.findOne(similarId)

                                        if ((similar?.id != submission?.id)
                                                && (similar.status == Submission.Status.CORRECT)) {
                                            Similarity similarity = similarityRepository.findBySubmissionAndSimilar(submission, similar)

                                            if (similarity) {
                                                similarity.rate = rate
                                            } else {
                                                similarity = new Similarity(submission: submission, similar: similar, rate: rate)
                                            }

                                            similarities.add(similarity)
                                        }
                                    }
                                }
                            }

                            submission.similarities = similarities
                            submissionRepository.save(submission)
                        }
                    }
                }
            }
        } catch (e) {
            e.printStackTrace()
        }

        latch.countDown()
    }
}
