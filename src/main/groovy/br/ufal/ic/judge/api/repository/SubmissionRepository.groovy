package br.ufal.ic.judge.api.repository

import br.ufal.ic.judge.api.domain.Submission
import org.springframework.data.repository.CrudRepository

interface SubmissionRepository extends CrudRepository<Submission, Long> {

}