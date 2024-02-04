public class GAPTabuMove {
    private int agent1;
    private int agent2;
    private int job1;
    private int job2;

    public GAPTabuMove(int agent1, int job1, int agent2, int job2) {
        this.agent1 = agent1;
        this.agent2 = agent2;
        this.job1 = job1;
        this.job2 = job2;
    }
    
    // equals
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        GAPTabuMove move = (GAPTabuMove) o;
        return (move.agent1 == this.agent1 && move.agent2 == this.agent2 && move.job1 == this.job1
                && move.job2 == this.job2)
                || (move.agent1 == this.agent2 && move.agent2 == this.agent1 && move.job1 == this.job2
                        && move.job2 == this.job1);
    }
    

    // get set
    public int getAgent1() {
        return this.agent1;
    }

    public void setAgent1(int agent1) {
        this.agent1 = agent1;
    }

    public int getAgent2() {
        return this.agent2;
    }

    public void setAgent2(int agent2) {
        this.agent2 = agent2;
    }

    public int getJob1() {
        return this.job1;
    }

    public void setJob1(int job1) {
        this.job1 = job1;
    }

    public int getJob2() {
        return this.job2;
    }

    public void setJob2(int job2) {
        this.job2 = job2;
    }


}
