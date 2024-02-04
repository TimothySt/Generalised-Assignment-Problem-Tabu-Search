public class Agent {
    double capacities;//pojemność zasobów agenta
    int id;//id agenta

    public Agent(int id, double capacities) {
        this.id = id;
        this.capacities = capacities;
    }
    

    public void print() {
        System.out.println("Agent " + id + " capacities: " + capacities);
    }


}
