package br.ufal.ic.judge.api.test.fuctional

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class UserSpec extends Specification {

    def client = new RESTClient("http://localhost:8080/")

    def "when request /users with method GET a json array of users should be returned"() {
        when:
        def response = client.get(path: "users")

        then:
        response.status == 200
        response.data.size == 10
    }

    def "when request /users/8 with method GET a json with users data should be returned"() {
        when:
        def response = client.get(path: "users/8")

        then:
        response.status == 200
        response.data.id == 8
        response.data.name == "Marcio Guimarães"
        response.data.username == "marcio"
        response.data.email == "marcioaugustosg@gmail.com"
        response.data.password == "secret"
    }

    def "when request /users with method POST and user's data a new user should be created and returned"() {
        when:
        def response = client.post(path: "users", body: [
                name: "Usuario Teste",
                username: "usuario",
                email: "marcioasguimaraes@gmail.com",
                password: "secret"
        ], requestContentType: ContentType.JSON)

        then:
        response.status == 200
        response.data.id == 11
        response.data.name == "Usuario Teste"
        response.data.username == "usuario"
        response.data.email == "marcioasguimaraes@gmail.com"
        response.data.password == "secret"
    }

    def "when request /users/11 with method PUT and users data in body a updated user should be returned"() {
        when:
        def response = client.put(path: "users/11", body: [
                name: "Usuario Teste 2",
                username: "usuario2",
                password: "secret"
        ], requestContentType: ContentType.JSON)

        then:
        response.status == 200
        response.data.id == 11
        response.data.name == "Usuario Teste 2"
        response.data.username == "usuario2"
        response.data.email == "marcioasguimaraes@gmail.com"
        response.data.password == "secret"
    }

    def "when request /users/8 with method DELETE shoud be returned NO CONTENT after delete user from database"() {
        when:
        def response = client.delete(path: "users/11")

        then:
        response.status == 204
    }


}
