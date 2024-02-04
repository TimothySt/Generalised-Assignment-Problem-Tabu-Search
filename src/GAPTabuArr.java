import java.util.ArrayList;

public class GAPTabuArr
{
    // rozmiar tabu
    private int tabuSize;
    // tablica tabu ruchów do prolemu GAP
    private ArrayList<GAPTabuArr> tabuArrList;

    public GAPTabuArr(int tabuSize)
    {
        this.tabuSize = tabuSize;
        tabuArrList = new ArrayList<GAPTabuArr>();
    }

    // czy ruch jest na liście tabu
    public boolean isTabu(GAPTabuArr GAPTabuArr)
    {
        boolean isTabu = false;
        for (GAPTabuArr GAPTabuArr2 : tabuArrList)
        {
            if (GAPTabuArr.equals(GAPTabuArr2)) {
                isTabu = true;
                break;
            }
        }
        return isTabu;
    }
    
    
    // dodanie ruchu do listy tabu
    public void add(GAPTabuArr GAPTabuArr)
    {
        // usunięcie ruchów z listy tabu, które przekroczyły kandecję
        

        // odanie nowego
        if (tabuSize <= tabuArrList.size())
        {
            tabuArrList.remove(0);
        }
        tabuArrList.add(GAPTabuArr);
    }

    // usunięcie ruchu z listy tabu
    public void remove(GAPTabuArr tb)
    {
        //znajdź indeks ruchu
        int index = -1;
        for (int i = 0; i < tabuArrList.size(); i++)
        {
            if (tb.equals(tabuArrList.get(i)))
            {
                index = i;
                break;
            }
        }
        //usuń ruch
        if (index != -1)
        {
            tabuArrList.remove(index);
        }
        else
        {
            System.out.println("Nie znaleziono ruchu na liście tabu");
            
        }

    }
}