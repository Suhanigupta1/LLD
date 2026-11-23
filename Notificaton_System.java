
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
    private final RetryExecutor retryExecutor = new RetryExecutor(3);

    NotificationService(List<NotificationSender> senders) {
        this.senders = List.copyOf(senders);
        
    }

    public void notifyUser(String recipient, String message) {
        for (NotificationSender sender : senders) {
          try{
            retryExecutor.execute(() -> {
                sender.send(recipient, message);
            });
          }catch (RuntimeException e){
            System.err.println("Notification permanently failed: "
                + e.getMessage());
            failures.add(new FailedNotification(recipient, message, 
            sender.getClass().getSimpleName()
            ));
          }
      }
    }
    public List<FailedNotification> getFailures() {
        return List.copyOf(failures);
    }
}
class RetryExecutor {
    private final int maxAttempts;

    RetryExecutor(int maxAttempts) {
        if (maxAttempts < 1) {
          throw new IllegalArgumentException(
            "maxAttempts must be >= 1"
          );
        }
        this.maxAttempts = maxAttempts;
    }

    public void execute(Runnable operation) {
        // TODO: Retry operation up to maxAttempts times
        // Stop immediately on success
        // Throw the final exception if all attempts fail
      for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                operation.run();
                return;
            } catch (RuntimeException e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt==maxAttempts){
                  throw e;
                }
            }
        }
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
