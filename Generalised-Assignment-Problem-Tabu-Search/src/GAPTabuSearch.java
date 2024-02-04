import java.util.ArrayList;
import java.util.Random;

public class GAPTabuSearch {
        //zmienne
        private Agent[] agents;//tablica agentów
        private double[] agentCapacity;//pojemności agentów
        private Job[] jobs;//tablica zadań
        private int n;//liczba agentów
        private int m;//liczba zadań
        
        /*
            solution - tablica binarna n x m
            n - liczba agentów
            m - liczba zadań]
            gdzie 1 oznacza przypisanie zadania m do agenta n, a 0 - nieprzypisanie.
            zadanie może być przypisane tylko do jednego agenta
            więc suma w wierszu musi być równa 1
        */
        private int[][] currentSolution;//tablica rozwiązań
        private int[][] bestSolution;//tablica najlepszych rozwiązań
        private double bestCost;//najlepszy koszt
        /* Maxymalna iteracja w pętli tabusearch */
        private int MAX_ITER;
        private int MINIMALIZATION;

        // generator liczb losowych
        private Random rand = new Random();

    /* KONSTRUKTOR */
    public GAPTabuSearch(Problem problem, int maxIter, boolean MINIMALIZATION) {
        this.MINIMALIZATION = MINIMALIZATION ? 1 : -1; // 1 dla minimalizacji, -1 dla maksymalizacji
        this.agents = problem.agents.clone();
        this.jobs = problem.jobs.clone();
        this.n = problem.n;
        this.m = problem.m;
        this.currentSolution = new int[n][m];
        this.agentCapacity = new double[n];
        for (int i = 0; i < n; i++) {
            agentCapacity[i] = agents[i].capacities;
        }
        this.MAX_ITER = maxIter;
        //wygenerowanie rozwiązania początkowego 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                currentSolution[i][j] = 0;
            }
        }
        this.bestSolution = new int[n][m];
        // this.bestCost = Double.MAX_VALUE;
    }

    /* ROZWIĄZANIA POCZĄTKOWE */
    //wygenerowanie rozwiązania początkowego
    public int[][] generateInitialSolutionMinResources(){
        int[][] newSolution = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newSolution[i][j] = 0;
            }
        }
        // dla każdego zadania
        for (int i = 0; i < m; i++) {
            // wybierz prace która wymaga najmniej zasobów
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
            // wyświetlenie wartości sum oraz capacities
            // System.out.println("Suma zużywanych zasobów: " + sum + " Maksymalna ilość zasobów: " + agents[i].capacities); /*DEBUG */
            if(sum > agents[i].capacities){
                correct = false;
                // System.out.println("Przekroczono limit zasobów"); /*DEBUG */
                //Naprawa rozwiązania
                //Wybierz ważące najwięcej zadanie
                int max = 0;
                for (int j = 0; j < m; j++) {
                    if(jobs[j].resources[i] > jobs[max].resources[i]){
                        max = j;
                    }
                }
                //Przypisz to zadanie do innego agenta
                for (int j = 0; j < n; j++) {
                    if(j != i){
                        boolean limit = false;
                        double sum2 = 0;
                        for (int k = 0; k < m; k++) {
                            sum2 += newSolution[j][k] * jobs[k].resources[j];
                        }
                        if(sum2 + jobs[max].resources[j] > agents[j].capacities){
                            limit = true;
                        }
                        if(!limit){
                            newSolution[j][max] = 1;
                            newSolution[i][max] = 0;
                            break;
                        }
                    }
                }
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

        return newSolution.clone();
    }

    /* SZUKANIE LEGALNEGO SĄSIĘDZTWA */
    /* Sąsiedztwo przez zamienienia agetna wykonującego pracę */
    private GAPTabuMove findLegalMoveSimple(int[][] solution) {
        boolean legal = false;
        int tryCount = 0;
        // inicjowanie zmiennych; -1 oznacza brak przypisania zadania lub agenta
        int agent1 = -1;
        int job1 = -1;
        int agent2 = -1;
        int job2 = -1;
        do
        {
            // znajdź losowe zadanie 1
            job1 = rand.nextInt(m);
            // znqajdź agnenta do którego jest przypisane zadanie
            agent1 = -1;
            for (int i = 0; i < n; i++) {
                if (solution[i][job1] == 1) {
                    agent1 = i;
                    break;
                }
            }
            // znajdź losowo innego agenta
            agent2 = rand.nextInt(n);
            job2 = -1;
            while (agent2 == agent1) {
                agent2 = rand.nextInt(n);
            }
            /* PRZENIESIENIE POJEDYNCZEJ PRACY */
            // czy można dodać to zadanie do agenta 2
            // sumowanie obciążenia agenta 2 wraz z dodaniem zadania 1
            double agent2Workload = 0;
            for (int i = 0; i < m; i++) {
                agent2Workload += solution[agent2][i] * jobs[i].resources[agent2];
            }
            // sprawdzenie czy agent 2 ma wystarczająco zasobów
            if (agent2Workload + jobs[job1].resources[agent2] <= agents[agent2].capacities) {
                legal = true;
            }

            /* ZAMIANA PRAC */
            if (!legal)// próba zamiany prac jeśli nie można przenieść
            {
                // lista prac wykonywanych przez agenta 2
                ArrayList agents2jobs = new ArrayList();
                for (int i = 0; i < m; i++) {
                    if (solution[agent2][i] == 1) {
                        agents2jobs.add(i);
                    }
                }
                if (!agents2jobs.isEmpty()) // jeśli agent 2 nie wykonuje żadnej pracy, to nie ma sensu zamieniać
                {
                    // wybierz losowe zadanie z listy
                    job2 = (int) agents2jobs.get(rand.nextInt(agents2jobs.size()));

                    /* czy zamiana prac jest legalna */
                    // obciążenie agenta 1 po usunięciu zadania 1 i dodaniu zadania 2
                    double workload1 = 0;

                    for (int i = 0; i < m; i++) {
                        if (i != job1) {
                            workload1 += solution[agent1][i] * jobs[i].resources[agent1];
                        } else {
                            workload1 += jobs[job2].resources[agent1];
                        }
                    }
                    // obciążenie agenta 2 po usunięciu zadania 2 i dodaniu zadania 1
                    double workload2 = 0;

                    for (int i = 0; i < m; i++) {
                        if (i != job2) {
                            workload2 += solution[agent2][i] * jobs[i].resources[agent2];
                        } else {
                            workload2 += jobs[job1].resources[agent2];
                        }
                    }
                    // sprawdzenie czy obciążenie agentów jest mniejsze niż ich pojemności
                    if (workload1 <= agents[agent1].capacities && workload2 <= agents[agent2].capacities) {
                        legal = true;
                    }
                }

            }
            
            // zabezpieczenie przed nieskończoną pętlą
            tryCount++;
            if(legal)
                break;

            if(tryCount > 1000)
                break;

        } while (true);

        if (legal) {
            return new GAPTabuMove(agent1, job1, agent2, job2);
        }
        return null;
    }
    
    
    
    //wyświetlenie rozwiązania
    public void printSolution(int[][] solution) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
        // wyświetlenie kosztu rozwiązania
        System.out.println("Całkowity koszt: " + ( calculateCost(solution) * MINIMALIZATION) );
    }

    //obliczenie kosztu rozwiązania
    public double calculateCost(int[][] solution) {
        double cost = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cost += solution[i][j] * jobs[j].costs[i];
            }
        }
        return cost * MINIMALIZATION;  // TODO: koszt ujemny dla maksymalizacji kosztu - kod napisyny dla minimalizacji
    }

    // wyświetlenie danych wejściopwych
    public void printInputData() {
        // wyświetlenie agentów
        System.out.println("Agenci:");
        for (int i = 0; i < n; i++) {
            agents[i].print();
        }
        // wyświetlenie zadań
        System.out.println("Zadania: kolumna - odpowiedni agent, wiersz - odpowiednie zadanie");
        // macierz kosztów
        System.out.println("Koszt wykonania:");
        for (int i = 0; i < n; i++) {
            System.out.print("Agent " + i + ":\t");
            for (int j = 0; j < m; j++) {
                System.out.print(jobs[j].costs[i] + " ");
            }
            System.out.println();
        }
        // macierz zasobów
        System.out.println("Potrzebna zasoby:");
        for (int i = 0; i < n; i++) {
            System.out.print("Agent " + i + ":\t");
            for (int j = 0; j < m; j++) {
                System.out.print(jobs[j].resources[i] + " ");
            }
            System.out.println();
        }

    }
    
    public void printBestSolution() {
        printSolution(bestSolution);
    }

    public void printBestSolutionValue() {
        System.out.println("Najlepsze rozwiązanie: " + bestCost * MINIMALIZATION);
    }
    public double getBestSolutionValue() {
        return bestCost * MINIMALIZATION;
    }

    //start
    public void Start() {
        /* ALGORYTM TABU SEATCH */
        // nowa lista tabu
        final int TABU_SIZE = 20; // kadencja tablicy tabu
        GAPTabuArr tabuArr = new GAPTabuArr(TABU_SIZE);

        //wygenerowanie rozwiązzania początkowego
        currentSolution = generateInitialSolutionMinResources();
        // System.out.println("Rozwiązanie początkowe:"); //DEBUG
        // printSolution(currentSolution);

        // przypisanie rozwiązania początkowego jako najlepsze
        bestSolution = currentSolution.clone();
        bestCost = calculateCost(currentSolution);

        // pętla algorytmu i do MAX_ITER
        for (int i = 0; i < MAX_ITER; i++) {
            // wyegnerowanie sąsiedztwa

            int[][] newSolution;// = currentSolution.clone(); // clone nie działa
            // przepisz rozwiązanie do nowego rozwiązania
            newSolution = new int[n][m];
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    newSolution[j][k] = currentSolution[j][k];
                }
            }

            // znalezienie ruchu sąsiedztwa
            // debug("Szukanie ruchu sąsiedztwa");
            GAPTabuMove move = findLegalMoveSimple(newSolution);
            /* SĄSIEDZTWO NULL -> ZERWIJ TABU SEARCH */
            if (move == null) {
                // debug("Brak sąsiedztwa");
                break;
            }
            
            // debug("Ruch sąsiedztwa: " + move.getAgent1() + " " + move.getJob1() + " " + move.getAgent2() + " " + move.getJob2());
            // wykonanie ruchu
            int agent1 = move.getAgent1();
            int job1 = move.getJob1();
            int agent2 = move.getAgent2();
            int job2 = move.getJob2();
            // usunięcie zadania 1 z agenta 1
            newSolution[agent1][job1] = 0;
            // dodanie zadania 1 do agenta 2
            newSolution[agent2][job1] = 1;
            // czy zadanie 2 istnieje
            if (job2 >= 0) {
                // usunięcie zadania 2 z agenta 2
                newSolution[agent2][job2] = 0;
                // dodanie zadania 2 do agenta 1
                newSolution[agent1][job2] = 1;
            }
            // oceń koszt nowego rozwiązania
            double newCost = calculateCost(newSolution);
            // czy ruch jest tabu i nie spełnia kryterium aspiracji (lepszy niż najlepsze), to pomiń
            boolean isTabu = tabuArr.isTabu(move);
            boolean isAspiration = newCost < bestCost;
            if (isTabu && !isAspiration) {
                continue;
            }
            // if (isTabu) /* DEBUG */
            // {
            //     debug("tabu: " + isTabu);
            //     debug("aspiration: " + isAspiration);
            // }

            // przypisanie mowego rozwiązania jako obecne
            currentSolution = newSolution.clone();
            // sprawdzenie czy nowe rozwiązanie jest lepsze od najlepszego
            if (calculateCost(currentSolution) < calculateCost(bestSolution)) {
                bestSolution = newSolution.clone();
                bestCost = calculateCost(bestSolution);
            }
            // uaaktualnij listę tabu
            tabuArr.add(move);

        }
        // wyświetlenie najlepszego rozwiązania
        // System.out.println("Najlepsze rozwiązanie:");
        // printSolution(bestSolution);
    }
    

    private static void debug(String string) {
        System.out.println(string);
    }
}
