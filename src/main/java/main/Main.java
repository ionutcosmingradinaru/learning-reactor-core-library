package main;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

public class Main {

  public static void main(String[] args) {
    upperStringFlux();
    consumerForFlux();
    subscriberForFlux();
  }

  private static void subscriberForFlux() {
    Flux<String> people = Flux.just("John", "Mary", "William");
    people
        .log()
        .subscribe(new Subscriber<>() {
          private long count = 0;
          private Subscription subscription;

          @Override
          public void onSubscribe(Subscription s) {
            this.subscription = s;
            s.request(2);
          }

          @Override
          public void onNext(String s) {
            count++;
            if (count >= 2) {
              count = 0;
              this.subscription.request(2);
            }
          }

          @Override
          public void onError(Throwable t) {

          }

          @Override
          public void onComplete() {

          }
        });
  }

  private static void consumerForFlux() {
    Flux<Person> people = Flux.just(
        new Person("John", 26),
        new Person("Mary", 23)
    );

    Consumer<Person> consumer = p -> {
      System.out.println("-----");
      System.out.println("The name is: " + p.getName());
      System.out.println("The age is: " + p.getAge());
      System.out.println("-----");
    };

    people
        .log()
        .subscribe(consumer);
  }

  private static void upperStringFlux() {
    Flux<String> flux = Flux.just("red", "yellow", "brown");
    var upperFlux = flux.map(String::toUpperCase);
    upperFlux
        .log()
        .map(String::toUpperCase)
        .subscribe(System.out::println);
  }
}

class Person {

  private final String name;
  private final int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}
