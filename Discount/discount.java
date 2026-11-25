
import java.util.*;

interface DiscountPolicy {
    double calculateDiscount(double amount);
}

class PremiumDiscount implements DiscountPolicy {
    @Override
    public double calculateDiscount(double amount) {
        return amount * 0.20;
    }
}

class FestivalDiscount implements DiscountPolicy {
    @Override
    public double calculateDiscount(double amount) {
        return amount * 0.10;
    }
}

class CouponDiscount implements DiscountPolicy {
    @Override
    public double calculateDiscount(double amount) {
        return 100;
    }
}

class CheckoutService {
    private final List<DiscountPolicy> policies;

    CheckoutService(List<DiscountPolicy> policies) {
        this.policies = List.copyOf(policies);
    }

    public double calculateFinalAmount(double amount) {
        if(!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException(
                "Invalid initial amount"
            );
        }
        double finalAmount = amount;
        for (DiscountPolicy policy : policies) {
            double discount =
                policy.calculateDiscount(finalAmount);

            if (!Double.isFinite(discount)
                    || discount < 0) {
                throw new IllegalArgumentException(
                    "Invalid discount"
                );
            }
            finalAmount -= Math.min(discount, finalAmount);
        }
        return finalAmount;
    }
}

class Main {
    public static void main(String[] args) {
        CheckoutService checkout = new CheckoutService(
            List.of(
                new PremiumDiscount(),
                new FestivalDiscount(),
                new CouponDiscount()
            )
        );

        System.out.println(
            checkout.calculateFinalAmount(1000)
        );
    }
}
