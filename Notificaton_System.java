
import java.util.*;

interface NotificationSender {
    void send(String recipient, String message);
}

class EmailSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Email sent: " + message);
    }
}

class SmsSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        throw new RuntimeException("SMS provider unavailable");
    }
}

class PushSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Push sent: " + message);
    }
}

class FailedNotification {
    private final String recipient;
    private final String message;
    private final String channel;

    FailedNotification(String recipient, String message,
                       String channel) {
        this.recipient = recipient;
        this.message = message;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}

class NotificationService {
    private final List<NotificationSender> senders;
    private final List<FailedNotification> failures =
            new ArrayList<>();

    NotificationService(List<NotificationSender> senders) {
        this.senders = List.copyOf(senders);
    }

    public void notifyUser(String recipient, String message) {
        for (NotificationSender sender : senders) {
          for (int attempt = 1; attempt <= 3; attempt++) {
              try {
                  sender.send(recipient, message); 
                  break;
              } catch (Exception e) {
                  System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                  if (attempt==3){
                    System.err.println(
                    "Failed: " + sender.getClass().getSimpleName()
                    + " - " + e.getMessage()
                    );
                    failures.add(new FailedNotification(
                        recipient,
                        message,
                        sender.getClass().getSimpleName()
                    ));
                  }
              }
          }
      }
    }
    public List<FailedNotification> getFailures() {
        return List.copyOf(failures);
    }
}

class Main {
    public static void main(String[] args) {
        NotificationService service =
            new NotificationService(
                List.of(
                    new EmailSender(),
                    new SmsSender(),
                    new PushSender()
                )
            );

        service.notifyUser("user@example.com", "Welcome!");

        System.out.println(
            "Failed notifications: "
            + service.getFailures().size()
        );
    }
}
