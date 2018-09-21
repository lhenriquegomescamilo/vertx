package io.vertx.book.message;


import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        final EventBus bus = vertx.eventBus();
        final Single<JsonObject> objectSingle = bus.<JsonObject>rxSend("hello", "Luke")
                .map(Message::body);

        final Single<JsonObject> objectSingle1 = bus.<JsonObject>rxSend("hello", "Leia")
                .map(Message::body);

        Single.zip(objectSingle, objectSingle1, (luke, leia) -> new JsonObject()
                .put("Luke", luke.getString("message"))
                .put("Leia", leia.getString("message")))
            .subscribe(
                    x -> System.out.println(x.encode()),
                    Throwable::printStackTrace
            );


    }

}
