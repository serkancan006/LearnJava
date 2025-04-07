package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");


        String bootstrapServers = "localhost:9092";
        String topic = "test-topic";

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Producer başlat\n2. Consumer başlat");
        String secim = scanner.nextLine();

        if (secim.equals("1")) {
            KafkaProducerService producer = new KafkaProducerService(bootstrapServers, topic);
            System.out.println("Mesaj girin (çıkmak için boş bırakın):");
            while (true) {
                String input = scanner.nextLine();
                if (input.isBlank()) break;
                producer.sendMessage(input);
            }
            producer.close();
        } else if (secim.equals("2")) {
            KafkaConsumerService consumer = new KafkaConsumerService(bootstrapServers, topic, "test-group");
            consumer.pollMessages(); // sonsuz döngüde çalışır
        }

    }
}