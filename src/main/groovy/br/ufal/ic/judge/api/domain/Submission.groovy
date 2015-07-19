package br.ufal.ic.judge.api.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Transient

@Entity
class Submission {

    @Transient
    def final WAITING = "WAITING"

    @Transient
    def final WRONG_ANSWER = "WRONG_ANSWER"

    @Transient
    def final CORRECT = "CORRECT"

    @Transient
    def final COMPILATION_ERROR = "COMPILATION_ERROR"

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id

    String code
    String output
    String language
    String status = WAITING
    Date dateCreate = new Date()

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user

}
