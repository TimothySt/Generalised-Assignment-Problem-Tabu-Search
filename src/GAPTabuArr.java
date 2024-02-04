import java.util.ArrayList;

public class GAPTabuArr
{
    // rozmiar tabu
    private int tabuSize;
    // tablica tabu ruchów do prolemu GAP
    private ArrayList<GAPTabuMove> tabuArray;

    public GAPTabuArr(int tabuSize)
    {
        this.tabuSize = tabuSize;
        tabuArray = new ArrayList<GAPTabuMove>();
    }

    public boolean isTabu(GAPTabuMove move)
    {
        return tabuArray.contains(move);
    }
    
    
    // dodanie ruchu do listy tabu
    public boolean add(GAPTabuMove move)
    {
        // usunięcie ruchów z listy tabu, które przekroczyły kandecję
        

        // odanie nowego
        if (tabuSize <= tabuArray.size())
        {
            tabuArray.remove(0);
        }
        return tabuArray.add(move);
    }

    // usunięcie ruchu z listy tabu
    public boolean remove(GAPTabuMove move)
    {
        return tabuArray.remove(move);
    }
    
    
    // wyczyszczenie tablicy
    public void clear()
    {
        tabuArray.clear();
    }
}