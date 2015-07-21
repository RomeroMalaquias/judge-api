package br.ufal.ic.judge.api.test.fuctional

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class SubmissionSpec extends Specification {

    def client = new RESTClient("http://localhost:8080/")

    def "enviando uma requisição para /submissions com o metódo POST e os dados de uma submissão correta"() {
        when:
        def response = client.post(path: "/submissions", body: [
                code    : "print 'Ola mundo'",
                output  : "Ola mundo",
                input   : "Ola mundo",
                language: "groovy",
                user    : [id: 1]
        ], requestContentType: ContentType.JSON)

        then:
        response.status == 200
        response.data.status == "WAITING"
        response.data.code == "print 'Ola mundo'"
        response.data.output == "Ola mundo"
        response.data.input == "Ola mundo"
        response.data.language == "groovy"
        response.data.id != null
        response.data.dateCreated != null
        response.data.user.id == 1
        response.data.user.name == "Anderson Santos"

        when:
        sleep(2000)
        response = client.get(path: "/submissions/$response.data.id")

        then:
        response.data.status == "CORRECT"

    }

    def "enviando uma requisição para /submissions com o metódo POST e os dados de uma submissão errada"() {
        when:
        def response = client.post(path: "/submissions", body: [
                code    : "print 'Ola mundo!'",
                output  : "Ola mundo",
                input   : "",
                language: "groovy",
                user    : [id: 8]
        ], requestContentType: ContentType.JSON)

        then:
        response.status == 200
        response.data.status == "WAITING"
        response.data.code == "print 'Ola mundo!'"
        response.data.output == "Ola mundo"
        response.data.input == ""
        response.data.language == "groovy"
        response.data.id != null
        response.data.dateCreated != null
        response.data.user.id == 8
        response.data.user.name == "Marcio Guimarães"

        when:
        sleep(2000)
        response = client.get(path: "/submissions/$response.data.id")

        then:
        response.data.status == "WRONG_ANSWER"

    }

    def "enviando uma requisição para /submissions com o metódo POST e os dados de uma submissão com erro de compilação"() {
        when:
        def response = client.post(path: "/submissions", body: [
                code    : "prin 'Ola mundo!'",
                output  : "Ola mundo",
                input   : "",
                language: "groovy",
                user    : [id: 8]
        ], requestContentType: ContentType.JSON)

        then:
        response.status == 200
        response.data.status == "WAITING"
        response.data.code == "prin 'Ola mundo!'"
        response.data.output == "Ola mundo"
        response.data.input == ""
        response.data.language == "groovy"
        response.data.id != null
        response.data.dateCreated != null
        response.data.user.id == 8
        response.data.user.name == "Marcio Guimarães"

        when:
        sleep(2000)
        response = client.get(path: "/submissions/$response.data.id")

        then:
        response.data.status == "COMPILATION_ERROR"

    }
}
