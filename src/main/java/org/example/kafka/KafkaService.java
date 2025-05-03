package org.example.kafka;

import org.example.anatations.Service;

import java.util.Scanner;

@Service
public class KafkaService {
    public void runKafkaApp() {
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
            consumer.pollMessages(); // Sonsuz döngüde çalışır
        } else {
            System.out.println("Geçersiz seçim!");
        }

        scanner.close();
    }

}
