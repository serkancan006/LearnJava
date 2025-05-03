package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.example.kafka.KafkaService;
import org.example.todoApp.DatabaseContext;
import org.example.todoApp.DatabaseInitializer;
import org.example.todoApp.repositories.Interfaces.INoteRepository;
import org.example.todoApp.repositories.Interfaces.IUserRepository;
import org.example.todoApp.repositories.NoteRepository;
import org.example.todoApp.repositories.UserRepository;

import java.util.Scanner;

public class AppContext extends AbstractModule {
    @Override
    protected void configure() {
        // Kafka
        bind(KafkaService.class).toInstance(new KafkaService());
        //bind(UserRepository.class).toInstance(new UserRepository());
        //bind(NoteRepository.class).toInstance(new NoteRepository());
        bind(IUserRepository.class).to(UserRepository.class);
        bind(INoteRepository.class).to(NoteRepository.class);
        bind(Scanner.class).toInstance(new Scanner(System.in));
        bind(DatabaseContext.class).in(Singleton.class);
        bind(DatabaseInitializer.class).in(Singleton.class);
    }
}
