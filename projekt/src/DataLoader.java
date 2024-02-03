import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DataLoader {
    private Problem[] problems;

    // Konstruktor
    public DataLoader() {
    }

    // Wczytujemy dane z pliku
    public void readData(String filename) throws IOException {
        String path = System.getProperty("user.dir");
        // System.out.println(path);
        // jezeli nie kończy sie na src to dodaj src
        if(!path.endsWith("src")){
            path+="\\src\\";
        }
        else{
            path+="\\";
        }
        path += "data\\" + filename;
        System.out.println(path);
        // Otwieramy plik do odczytu
        BufferedReader reader = new BufferedReader(new FileReader(path));

        // Wczytujemy liczbę zestawów problemów
        int numProblemSets = Integer.parseInt(reader.readLine().trim());

        // Zdefiniowanie tablicy problemów
        Problem[] problems = new Problem[numProblemSets];

         // Dla każdego zestawu problemów
         for (int p = 0; p < numProblemSets; p++) {
            // Wczytujemy liczbę agentów i zadań
            String[] line = reader.readLine().trim().split(" ");
            int numAgents = Integer.parseInt(line[0]);
            int numJobs = Integer.parseInt(line[1]);
            
            // Inicjalizujemy tablice agentów i zadań
            Agent[] agents = new Agent[numAgents];
            Job[] jobs = new Job[numJobs];
            
            // Inicjalizujemy tablice do przechowywania kosztów i zasobów
            double[][] costs = new double[numAgents][numJobs];
            double[][] resources = new double[numAgents][numJobs];
            
            // Dla każdego agenta
            for (int i = 0; i < numAgents; i++) {
                // Wczytujemy koszty przydzielania zadań
                line = reader.readLine().trim().split(" ");
                for (int j = 0; j < numJobs; j++) {
                    costs[i][j] = Double.parseDouble(line[j]);
                }
            }
            
            // Dla każdego agenta
            for (int i = 0; i < numAgents; i++) {
                // Wczytujemy zasoby zużywane przez zadania
                line = reader.readLine().trim().split(" ");
                for (int j = 0; j < numJobs; j++) {
                    resources[i][j] = Double.parseDouble(line[j]);
                }
            }
            
            // Wczytujemy pojemności zasobów agentów
            line = reader.readLine().trim().split(" ");
            double[] capacities = new double[numAgents];
            for (int i = 0; i < numAgents; i++) {
                capacities[i] = Double.parseDouble(line[i]);
            }

            // Tworzymy obiekty agentów
            for (int i = 0; i < numAgents; i++) {
                agents[i] = new Agent(i, capacities[i]);
            }

            // Tworzymy obiekty zadań
            for (int i = 0; i < numJobs; i++) {
                double[] resources_i = new double[numAgents];
                for (int j = 0; j < numAgents; j++) {
                    resources_i[j] = resources[j][i];
                }
                double[] costs_i = new double[numAgents];
                for (int j = 0; j < numAgents; j++) {
                    costs_i[j] = costs[j][i];
                }
                jobs[i] = new Job(i, costs_i, resources_i);
            }

            // Tworzymy obiekt problemu
            problems[p] = new Problem(agents, jobs, numAgents, numJobs);
            
        }
        this.problems = problems.clone();
        
        // Zamykamy plik
        reader.close();
    }

    // Zrwacamy tablicę problemów
    public Problem[] getProblems() {
        return  problems.clone();
    }

    // Zwróć problem o indeksie i
    public Problem getProblem(int i) {
        return new Problem(problems[i].agents.clone(), problems[i].jobs.clone(), problems[i].n, problems[i].m);
    }

    public void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader();
        dataLoader.readData("gap1.txt");
        Problem[] problems = dataLoader.getProblems();
        Problem problem1 = problems[0];
    }
}
