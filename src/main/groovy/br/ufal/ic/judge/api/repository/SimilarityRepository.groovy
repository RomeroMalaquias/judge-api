package br.ufal.ic.judge.api.repository

import br.ufal.ic.judge.api.domain.Similarity
import br.ufal.ic.judge.api.domain.Submission
import org.springframework.data.repository.CrudRepository

interface SimilarityRepository extends CrudRepository<Similarity, Long> {

    Similarity findBySubmissionAndSimilar(Submission submission, Submission similar)


}