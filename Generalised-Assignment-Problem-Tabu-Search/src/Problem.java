public class Problem {
    public Agent[] agents;//tablica agentów
    public Job[] jobs;//tablica zadań
    public int n;//liczba agentów
    public int m;//liczba zadań
    
    public Problem(Agent[] agents, Job[] jobs, int n, int m) {
        this.agents = agents;
        this.jobs = jobs;
        this.n = n;
        this.m = m;
    }
    public Problem() {
    }
    public Problem(Agent[] agents, Job[] jobs) {
        this.agents = agents;
        this.jobs = jobs;
        this.n = agents.length;
        this.m = jobs.length;
    }

    // Wyświetl problem
    public void print() {
        System.out.println("Problem:");
        System.out.println("Liczba agentów: " + n);
        System.out.println("Liczba zadań: " + m);
        System.out.println("Agenci:");
        for (int i = 0; i < n; i++) {
            agents[i].print();
        }
        System.out.println("Zadania:");
        for (int i = 0; i < m; i++) {
            jobs[i].print();
        }
    }
}
