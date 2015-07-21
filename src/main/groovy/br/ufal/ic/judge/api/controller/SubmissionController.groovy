package br.ufal.ic.judge.api.controller

import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.repository.SubmissionRepository
import br.ufal.ic.judge.api.repository.UserRepository
import br.ufal.ic.judge.api.service.EmailService
import br.ufal.ic.judge.api.service.EvaluatorService
import br.ufal.ic.judge.api.service.SimilarityService
import org.hibernate.validator.constraints.Email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/submissions")
class SubmissionController {

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    EvaluatorService evaluatorService

    @Autowired
    SimilarityService similarityService

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    def get(@PathVariable Long id) {
        submissionRepository.findOne(id)
    }

    @RequestMapping(method = RequestMethod.GET)
    def list() {
        submissionRepository.findAll()
    }

    @RequestMapping(method = RequestMethod.POST)
    def save(@RequestBody Submission submission) {

        Submission submissionInstance = null

        try {
            submissionInstance = new Submission(
                    code: submission.code,
                    output: submission.output,
                    input: submission.input,
                    language: submission.language,
                    user: userRepository.findOne(submission.user.id)
            )

            submissionInstance = submissionRepository.save(submissionInstance)
        } catch(Exception e) {
            e.printStackTrace()
            new ResponseEntity<String>(HttpStatus.BAD_REQUEST)
        }

        if (submissionInstance?.id) {
            evaluatorService.evaluate(submissionInstance)
            similarityService.update()
            submissionInstance
        } else {
            new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping(value = "/{id}/similarities", method = RequestMethod.GET)
    def listSimilarities(@PathVariable Long id) {

        Submission submissionInstance = submissionRepository.findOne(id)

        if (submissionInstance) {
            submissionInstance.similarities
        } else {
            new ResponseEntity<String>(HttpStatus.NOT_FOUND)
        }
    }

}
