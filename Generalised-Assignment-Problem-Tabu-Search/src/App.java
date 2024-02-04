import java.io.File;
import java.io.FileWriter;

public class App {

    static final boolean MAXIMALIZATION = false;
    static final boolean MINIMALIZATION = true;
    static String currentDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws Exception {

        // DataLoader dt = new DataLoader();
        // dt.readData("gap2.txt");

        // ts("gap12.txt");
        

        
        String folderPath = "\\src\\data\\";
        
        // wykonaj algorytm dla wszystkich plików w folderze
        File folder = new File(currentDirectory + folderPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                ts(filename, MINIMALIZATION, 10);
            }
        }
    }

    public static void ts(String filename, boolean MINIMAZATION, int wykonania)
    {
        // parametry algorytmu
        int maxIter = 100000; // maksymalna liczba iteracji algorytmu tabu search
        
        System.out.println();
        System.out.println("Wczytuję plik: " + filename);

        // wczytanie danych
        DataLoader dataLoader = new DataLoader();
        try
        {
            dataLoader.readData(filename);
        }
        catch (Exception e) {
            System.out.println("Nie znaleziono pliku: " + filename);
            return;
        }
        Problem[] problems = dataLoader.getProblems();

        // wykonanie algorytmu dla wszystkich problemów
        // tablica rozwiązań dla każdego problemu dla każdego wykonania
        double[][] results = new double[problems.length][wykonania];
        System.out.println();
        for (int i = 0; i < problems.length; i++) {

            Problem problem = problems[i];
            // WARTOŚĆ BOOLEAN OKREŚLA CZY WYŚWIETLAĆ WYNIKI MAJĄ BYĆ MINIMALIZOWANE CZY MAXYMALIZOWANE
            // przy maksy

            GAPTabuSearch ts = new GAPTabuSearch(problem, maxIter, MINIMAZATION);

            System.out.println("Problem " + (i + 1)+": ");
            for (int j = 0; j < wykonania; j++) {
                ts.Start();
                results[i][j] = ts.getBestSolutionValue();

                /* Wyświetlenie wyników */
                ts.printBestSolutionValue();
                // // ts.printBestSolution(); // wyświetla całe rozwiązanie
            }

        }
        // zapisanie danych do pliku wynikowego
        // czy minimalizowane czy maksymalizowane
        String mod;
        if(MINIMAZATION)
        {
            mod = "min";
        }
        else
        {
            mod = "max";
        }
        // nazwa pliku wynikowego:
        String outputFilename = filename.substring(0, filename.length() - 4) + "_" + mod + "_results.txt";
        // lokalizacja pliku wynikowego
        String outputPath = currentDirectory + "\\src\\results\\" + outputFilename;
        // zapisanie wyników do pliku
        try
        {
            // zapisanie wyników do pliku, jeśli już istnieje to nadpisze

            FileWriter writer = new FileWriter(outputPath);
            writer.write("Wyniki dla pliku: " + filename + "\n");
            writer.write("Liczba wykonan: " + wykonania + "\n");
            writer.write("Liczba problemów: " + problems.length + "\n");
            writer.write("Maksymalna liczba iteracji: " + maxIter + "\n");
            writer.write("Minimalizacja: " + MINIMAZATION + "\n");
            writer.write("\n");
            for (int i = 0; i < problems.length; i++) {
                writer.write("Problem " + (i + 1) + ":\n");
                for (int j = 0; j < wykonania; j++) {
                    // writer.write("Wykonanie " + (j + 1) + ": " + results[i][j] + "\n");
                    writer.write(results[i][j] + "\n");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("Wyniki zapisano do pliku: " + outputPath);
        } catch (Exception e) {
            System.out.println("Nie udało się zapisać wyników do pliku: " + outputPath);
        }
        
        
    }
}
