package br.ufal.ic.judge.api.rabbitmq

import java.util.concurrent.CountDownLatch

class Receiver {

    CountDownLatch latch = new CountDownLatch(1);

    def receiveMessage(String message) {
        println "Received <$message>"
        latch.countDown()
    }

}
