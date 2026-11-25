import java.util.*;
import java.util.function.Supplier;
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements NotificationSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class PushSender implements NotificationSender {
    public void send(String message) {
        System.out.println("Push: " + message);
    }
}

class NotificationFactory {
    private final Map<String, Supplier<NotificationSender>>
        registry = new HashMap<>();

    public void register(
        String type,
        Supplier<NotificationSender> supplier
    ) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Invalid type");
        }  
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        if (registry.putIfAbsent(type, supplier) != null) {
            throw new IllegalArgumentException(
                "Type already registered: " + type
            );
        }
    }

    public NotificationSender createSender(String type) {
      Supplier<NotificationSender> supplier = registry.get(type);
      if (supplier == null) {
          throw new IllegalArgumentException("Supplier type is invalid: " + type);
      }
      return supplier.get();
  }
}

class Main {
    public static void main(String[] args) {
        NotificationFactory factory =
            new NotificationFactory();

        factory.register("EMAIL", EmailSender::new);
        factory.register("SMS", SmsSender::new);
        factory.register("PUSH", PushSender::new);

        NotificationSender sender =
            factory.createSender("SMS");

        sender.send("Welcome!");
    }
}

