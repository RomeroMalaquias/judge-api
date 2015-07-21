package br.ufal.ic.judge.api.domain

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonFormat

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Submission {

    enum Status {
        WAITING,
        CORRECT,
        WRONG_ANSWER,
        COMPILATION_ERROR
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id

    String code
    String output
    String input
    String language
    Status status = Status.WAITING
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    Date dateCreated = new Date()

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user

}
