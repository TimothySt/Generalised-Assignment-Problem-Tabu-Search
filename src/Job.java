public class Job {
    double[] costs;//tablica kosztów przydzielania zadań do agentów
    double[] resources;//tablica zasobów zużywanych przez zadania
    int id;//id zadania

    public Job(int id, double[] costs, double[] resources) {
        this.id = id;
        this.costs = costs;
        this.resources = resources;
    }

    public Job() {
    }

    public void print() {
        System.out.println("Zadanie " + id + ":");
        System.out.println("Koszty:");
        for (int i = 0; i < costs.length; i++) {
            System.out.print(costs[i] + " ");
        }
        System.out.println();
        System.out.println("Zasoby:");
        for (int i = 0; i < resources.length; i++) {
            System.out.print(resources[i] + " ");
        }
        System.out.println();
    }
}
