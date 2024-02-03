import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.crypto.Data;

public class TS {
    //zmienne
    private Problem[] problems;//tablica problemów
    private Agent[] agents;//tablica agentów
    private Job[] jobs;//tablica zadań
    private int n;//liczba agentów
    private int m;//liczba zadań
    private int[][] currentSolution;//tablica rozwiązań
    private int[][] bestSolution;//tablica najlepszych rozwiązań
    private double bestCost;//najlepszy koszt



    //konstruktor
    public TS(String filename) throws IOException {
        DataLoader dataLoader = new DataLoader();
        dataLoader.readData(filename);
        Problem[] problems =  dataLoader.getProblems();
        this.problems = problems;
    }

    //inicjalizacja
    public void initialize(Problem problem){
        this.agents = problem.agents.clone();
        this.jobs = problem.jobs.clone();
        this.n = problem.n;
        this.m = problem.m;
        this.currentSolution = new int[n][m];
        //wygenerowanie rozwiązania początkowego 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                currentSolution[i][j] = 0;
            }
        }
        this.bestSolution = new int[n][m];
        this.bestCost = Double.MAX_VALUE;        
    }

    //wygenerowanie rozwiązania początkowego
    public void generateInitialSolutionMinResources(){
        int[][] newSolution = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newSolution[i][j] = 0;
            }
        }
        // dla każdego zadania
        for (int i = 0; i < m; i++) {
            // wybierz agenta z najmniejszymi zasobami
            int min = 0;
            for (int j = 0; j < n; j++) {
                if(jobs[i].resources[j] < jobs[i].resources[min]){
                    //Sprawdzenie czy agent nie osiągnął limitu zasobów
                    boolean limit = false;
                    double sum = 0;
                    for (int k = 0; k < m; k++) {
                        sum += newSolution[j][k] * jobs[k].resources[j];
                    }
                    if(sum + jobs[i].resources[j] > agents[j].capacities){
                        limit = true;
                    }
                    if(!limit)
                    min = j;
                }
            }
            newSolution[min][i] = 1;
        }

        //Sprawdzenie czy rozwiązanie jest poprawne, nie przekracza limitów zasobów oraz każde zadanie jest przydzielone oraz każdy agent ma przypisane zadanie
        boolean correct = true;
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < m; j++) {
                sum += newSolution[i][j] * jobs[j].resources[i];
            }
            if(sum > agents[i].capacities){
                correct = false;
            }

            if(sum==0){
                //Naprawa rozwiązania
                //Wybierz losowo zadanie
                while (true) {
                    int randomJob = (int)(Math.random() * m);
                    //Znajdz agenta który ma przypisane to zadanie
                    int index = 0;
                    for (int j = 0; j < n; j++) {
                        if(newSolution[j][randomJob] == 1){
                            index = j;
                            break;
                        }
                    }
                //Sprawdz czy agent do którego jest obecnie przypisane zadanie posiada inne zadania 
                int job = 0;
                for (int j = 0; j < m; j++) {
                    if(newSolution[index][j] == 1){
                        job++;
                    }
                }
                boolean limit = false;
                double sum2 = 0;
                for (int k = 0; k < m; k++) {
                    sum2 += newSolution[i][k] * jobs[k].resources[i];
                }
                if(sum2 + jobs[randomJob].resources[i] > agents[i].capacities){
                    limit = true;
                }
                //Jeśli agent posiada inne zadania to przypisz mu wylosowane zadanie oraz usuń to zadanie z poprzedniego agenta
                if(job > 1 && !limit){
                    newSolution[i][randomJob] = 1;
                    for (int j = 0; j < m; j++) {
                        if(newSolution[i][j] == 1 && j != randomJob){
                            newSolution[i][j] = 0;
                        }
                    }
                    break;
                }
                }
                

            }
        }

        currentSolution = newSolution.clone();
    }

    //wyświetlenie rozwiązania
    public void printSolution(int[][] solution){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
    }

    //start
    public void Start(){
        for (int i = 0; i < problems.length; i++) {
            System.out.println("Problem " + i + ":");
            initialize(problems[i]);
            //wygenerowanie rozwiązzania początkowego
            generateInitialSolutionMinResources();
            System.out.println("Rozwiązanie początkowe:");
            printSolution(currentSolution);
        }
    }


    
    
}
