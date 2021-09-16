import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompetitionTests {

    @Test
    public void testDijkstraConstructor() {
    	CompetitionDijkstra dijkstra = new CompetitionDijkstra("\"C:\\Users\\Gregory Partridge\\Documents\\A&D\\tinyEWD.txt", 75, 89, 71);
        assertEquals(-1, dijkstra.timeRequiredforCompetition());

        dijkstra = new CompetitionDijkstra("\"C:\\Users\\Gregory Partridge\\Documents\\A&D\\1000EWD.txt", 65, 89, 61);
        assertEquals(-1, dijkstra.timeRequiredforCompetition());
        
        dijkstra = new CompetitionDijkstra(null, 73, 76, 81);
        assertEquals(-1, dijkstra.timeRequiredforCompetition());

    }

    @Test
    public void testFWConstructor() {
    	CompetitionFloydWarshall FW = new CompetitionFloydWarshall("\"C:\\Users\\Gregory Partridge\\Documents\\A&D\\tinyEWD.txt", 56, 88, 61);
        assertEquals(-1, FW.timeRequiredforCompetition());
        
        FW = new CompetitionFloydWarshall("\"C:\\Users\\Gregory Partridge\\Documents\\A&D\\1000EWD.txt", 56, 88, 61);
        assertEquals(-1, FW.timeRequiredforCompetition());
        
        FW = new CompetitionFloydWarshall(null, 56, 88, 61);
        assertEquals(-1, FW.timeRequiredforCompetition()); 
    }
    
}