package br.ufal.ic.judge.api.controller

import br.ufal.ic.judge.api.domain.Submission
import br.ufal.ic.judge.api.repository.SubmissionRepository
import br.ufal.ic.judge.api.service.EvaluatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@RequestMapping("/submissions")
class SubmissionController {

    @Autowired
    SubmissionRepository submissionRepository

    @Autowired
    EvaluatorService evaluatorService

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
        submissionRepository.save(submission)
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    def update(@PathVariable Long id, @RequestBody Submission submission) {

        Submission submissionInstance = submissionRepository.findOne(id)

        submissionInstance.code = submission.code ?: submissionInstance.code
        submissionInstance.output = submission.output ?: submissionInstance.output
        submissionInstance.language = submission.language ?: submissionInstance.language
        submissionInstance.status = submission.status ?:submissionInstance.status

        submissionRepository.save(submissionInstance)

        evaluatorService.evaluate(submissionInstance)

        submissionInstance
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    def delete(@PathVariable Long id) {

        Submission submissionInstance = submissionRepository.findOne(id)

        if (submissionInstance) {
            submissionRepository.delete(submissionInstance)
            new ResponseEntity<String>(HttpStatus.NO_CONTENT)
        } else {
            new ResponseEntity<String>(HttpStatus.NOT_FOUND)
        }
    }

}
