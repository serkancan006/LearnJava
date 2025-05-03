package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.example.anatations.Component;
import org.example.anatations.Repository;
import org.example.anatations.Service;
import org.example.kafka.KafkaService;
import org.example.todoApp.DatabaseContext;
import org.example.todoApp.DatabaseInitializer;
import org.example.todoApp.repositories.Interfaces.INoteRepository;
import org.example.todoApp.repositories.Interfaces.IUserRepository;
import org.example.todoApp.repositories.NoteRepository;
import org.example.todoApp.repositories.UserRepository;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Scanner;
import java.util.Set;

public class AppContext extends AbstractModule {
    @Override
    protected void configure() {
        bind(Scanner.class).toInstance(new Scanner(System.in));
        // Kafka
        //bind(KafkaService.class).toInstance(new KafkaService());
        //bind(UserRepository.class).toInstance(new UserRepository());
        //bind(NoteRepository.class).toInstance(new NoteRepository());
        //bind(IUserRepository.class).to(UserRepository.class);
        //bind(INoteRepository.class).to(NoteRepository.class);
        //bind(DatabaseContext.class).in(Singleton.class);
        //bind(DatabaseInitializer.class).in(Singleton.class);

        scanAndBindAnnotatedClasses("org.example");
    }

    private void scanAndBindAnnotatedClasses(String basePackage) {
        // Reflections ile belirtilen paketteki ve alt paketlerdeki sınıfları tara
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);

        // `@Repository`, `@Service`, ve `@Component` anotasyonuna sahip sınıfları al
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Repository.class);
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Component.class));

        for (Class<?> clazz : annotatedClasses) {
            // Sınıfın implement ettiği arayüzleri kontrol et
            Class<?>[] interfaces = clazz.getInterfaces();

            if (interfaces.length > 0) {
                // Interface'e bind et
                System.out.println("Binding interface " + interfaces[0].getSimpleName() + " to " + clazz.getSimpleName());
                bind(interfaces[0]).to((Class) clazz).in(Singleton.class);
            } else {
                // Sadece sınıfı bind et
                System.out.println("Binding class " + clazz.getSimpleName());
                bind((Class) clazz).in(Singleton.class);
            }
        }
    }
}
