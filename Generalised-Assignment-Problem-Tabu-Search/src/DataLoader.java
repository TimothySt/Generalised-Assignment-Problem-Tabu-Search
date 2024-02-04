import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {
    public String filename;
    private Problem[] problems;

    // Konstruktor
    public DataLoader() {
    }

    // Wczytujemy dane z pliku
    public void readData(String filename) throws IOException {
        this.filename = filename;
        /* tworzenie ścieżki do pliku */
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
            /* WCZYTYWANIE DANYCH Z PLIKU */
            // Wczytujemy liczbę agentów i zadań
            String[] line = reader.readLine().trim().split(" ");
            int numAgents = Integer.parseInt(line[0]);
            int numJobs = Integer.parseInt(line[1]);
            
            // Inicjalizujemy tablice agentów i zadań
            Agent[] agents = new Agent[numAgents];
            Job[] jobs = new Job[numJobs];
            /* DEBUG */
            // System.out.println("Liczba agentów: " + numAgents);
            // System.out.println("Liczba zadań: " + numJobs);
            
            // Inicjalizujemy tablice do przechowywania kosztów i zasobów
            double[][] costs = new double[numAgents][numJobs];
            double[][] resources = new double[numAgents][numJobs];
            
            // // Dla każdego agenta
            // for (int i = 0; i < numAgents; i++) {
            //     // Wczytujemy koszty przydzielania zadań
            //     line = reader.readLine().trim().split(" ");
            //     for (int j = 0; j < numJobs; j++) {
            //         costs[i][j] = Double.parseDouble(line[j]);
            //     }
            // }

            // Dla każdego agenta wczytujemy koszty przydzielania zadań ale w inny sposób, nie linia po linii, tylko wartość po wartości
            for (int i = 0; i < numAgents; i++) {
                // Wczytujemy koszty przydzielania zadań
                line = reader.readLine().trim().split(" ");

                if (line.length != numJobs)
                {
                    do
                    {
                        String[] line2 = reader.readLine().trim().split(" ");
                        // połącz obie linie
                        String[] line3 = new String[line.length + line2.length];

                        
                        
                        // wypełnij przpisz dane z obu linii do jednej
                        int k = 0;
                        for (int j = 0; j < line.length; j++) {
                            line3[k] = line[j];
                            k++;
                        }
                        for (int j = 0; j < line2.length; j++) {
                            line3[k] = line2[j];
                            k++;
                        }
                        line = line3;

                    } while (line.length < numJobs);
                    // czy teraz jest równe numJobs?
                    if (line.length != numJobs) {
                        System.out.println("Niepoprawna liczba kosztów dla agenta " + i);
                        return;
                    }
                }

                for (int j = 0; j < numJobs; j++) {
                    costs[i][j] = Double.parseDouble(line[j]);
                }
            }
            
            
            
            // // Dla każdego agenta
            // for (int i = 0; i < numAgents; i++) {
            //     // Wczytujemy zasoby zużywane przez zadania
            //     line = reader.readLine().trim().split(" ");
            //     for (int j = 0; j < numJobs; j++) {
            //         resources[i][j] = Double.parseDouble(line[j]);
            //     }
            // }
            
            // Dla każdego agenta wczytujemy zasoby zużywane przez zadania ale w inny sposób, nie linia po linii, tylko wartość po wartości
            for (int i = 0; i < numAgents; i++) {
                // Wczytujemy zasoby zużywane przez zadania
                line = reader.readLine().trim().split(" ");

                if (line.length != numJobs)
                {
                    do
                    {
                        String[] line2 = reader.readLine().trim().split(" ");
                        // połącz obie linie
                        String[] line3 = new String[line.length + line2.length];

                        
                        
                        // wypełnij przpisz dane z obu linii do jednej
                        int k = 0;
                        for (int j = 0; j < line.length; j++) {
                            line3[k] = line[j];
                            k++;
                        }
                        for (int j = 0; j < line2.length; j++) {
                            line3[k] = line2[j];
                            k++;
                        }
                        line = line3;

                    } while (line.length < numJobs);
                    // czy teraz jest równe numJobs?
                    if (line.length != numJobs) {
                        System.out.println("Niepoprawna liczba zasobów dla agenta " + i);
                        return;
                    }
                }

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

            /* BUDOWANIE PROBLEMÓW */

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
