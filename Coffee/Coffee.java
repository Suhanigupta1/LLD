import java.util.*;
interface Coffee {
    double getCost();
    String getDescription();
}

class BasicCoffee implements Coffee{
    private final double cost; 
    
    public BasicCoffee(double cost){
      this.cost= cost;
    }

    public double getCost(){
        return cost;
    }
    public String getDescription(){
        return "BasicCoffee";
    }

}

class MilkDecorator implements Coffee{
    private final Coffee wrapped;

    public MilkDecorator(Coffee wrapped){
        this.wrapped = Objects.requireNonNull(wrapped);
    }

    public double getCost(){
        return wrapped.getCost() + 20;
    }
    public String getDescription(){
        return wrapped.getDescription() + " with milk";
    }
}
class WhippedCreamDecorator implements Coffee{
    private final Coffee wrapped;
    
    public WhippedCreamDecorator(Coffee wrapped){
      this.wrapped = Objects.requireNonNull(wrapped);
    }

    public double getCost(){
        return wrapped.getCost() + 30;
    }

    public String getDescription(){
      return wrapped.getDescription() + " with whipped cream";
    }
}


class Main{
  public static void main(String args[]){
    Coffee coffee = new WhippedCreamDecorator(new MilkDecorator( new
    BasicCoffee(100)));
    System.out.println(coffee.getCost());
  }
}

