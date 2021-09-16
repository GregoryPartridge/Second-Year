import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestants’
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *     Each contestant walks at a given estimated speed.
 *     The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

class Intersection{
	ArrayList<Street> streets;
	Intersection()
	{
		streets = new ArrayList<Street>();
	}
	
}
class Street{
	int destination;
	double distance;
	Street(int destination, double distance)
	{
		this.destination = destination;
		this.distance = distance;
	}
}

public class CompetitionDijkstra {

	private int sA;
	private int sB;
	private int sC;
	private Intersection[] cityIntersections;
    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
    */
    CompetitionDijkstra (String filename, int sA, int sB, int sC)
    {
    	File file;
    	if(filename != null)
    	{
    		file = new File(filename);
    	}
    	else
    	{
    		file = null;
    	}
    	this.sA = sA;
    	this.sB = sB;
    	this.sC = sC;
    	try {
			BufferedReader inputFile = new BufferedReader(new FileReader(file));
			String IntersectionNumber = inputFile.readLine();
			
			int amountOfIntersections = Integer.parseInt(IntersectionNumber);
			this.cityIntersections = new Intersection[amountOfIntersections];
			
			for(int i=0; i<cityIntersections.length;i++)
			{
				cityIntersections[i] = new Intersection();
			}
			
			IntersectionNumber = inputFile.readLine();
			int streetsNumber = Integer.parseInt(IntersectionNumber);
			int position = 0;
			
			for(int i=0; i < streetsNumber; i++)
			{
				position = 0;
				IntersectionNumber = inputFile.readLine();
				if(IntersectionNumber != null) 
				{
					String[] numbers = IntersectionNumber.split("\\s+");
					if(numbers[0].equals(""))
					{
						position++;
					}
					int originalPoint = Integer.parseInt(numbers[position++]);
					int destination = Integer.parseInt(numbers[position++]);
					double distance = Double.parseDouble(numbers[position++]);
					cityIntersections[originalPoint].streets.add(new Street(destination,distance));
				}
			}
			inputFile.close();
		} 
    	catch (Exception e) 
    	{
			this.cityIntersections = new Intersection[0];
		}
    }


    /**
    * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition()
    {
    	
    	double shortestTime = -1;
    	int slowestSpeed = Math.min(sA, sB);
    	slowestSpeed = Math.min(slowestSpeed, sC);
    	if(slowestSpeed < 50 || slowestSpeed > 100)
    	{
    		return -1;
    	}
    	for(int counter=0; counter < cityIntersections.length; counter++)
    	{

    		double[] distanceTo = new double[cityIntersections.length];
    		int edgeTo[] = new int[cityIntersections.length];
    		
    		ArrayList<Integer> queue = new ArrayList<Integer>();
    		ArrayList<Integer> visited = new ArrayList<Integer>();
    		queue.add(counter);
    		for(int x=0; x < edgeTo.length; x++)
    		{
    			edgeTo[x] = -1;
    			distanceTo[x] = -1;
    		}
    		distanceTo[counter] = 0;
    		edgeTo[counter] = counter;
    		while(!queue.isEmpty())
    		{
    			int minimumIndex = getMin(queue, distanceTo);
    			int IntersectionNumber = queue.remove(minimumIndex);
    			Intersection currentIntersection = cityIntersections[IntersectionNumber];
    			for(int j=0; j<currentIntersection.streets.size(); j++)
    			{
    				Street relaxedStreet = currentIntersection.streets.get(j);
    				if(relaxedStreet.distance + distanceTo[IntersectionNumber] < distanceTo[relaxedStreet.destination] || distanceTo[relaxedStreet.destination] == -1)
    				{
    					distanceTo[relaxedStreet.destination] = relaxedStreet.distance + distanceTo[IntersectionNumber];
    					edgeTo[relaxedStreet.destination] = IntersectionNumber;
    				}   
    				if(!visited.contains(relaxedStreet.destination) && !queue.contains(relaxedStreet.destination))
    					queue.add(relaxedStreet.destination);
    			}
    			visited.add(IntersectionNumber);
    			
    		}
    		
    		Arrays.sort(distanceTo);
    		double longestDistance = distanceTo[distanceTo.length-1];
    		boolean isConnectedCity = true;
    		for(int l=0;l<edgeTo.length;l++)
    		{
    			if(edgeTo[l] == -1)
    				isConnectedCity = false;
    		}
    		if(shortestTime == -1)
    		{
    			shortestTime = (longestDistance*1000)/slowestSpeed;
    		}
    		else if(!isConnectedCity)
    			return -1;
    		else
    			shortestTime = Math.max(shortestTime, (longestDistance*1000)/slowestSpeed);
    	}
    		
    	
        return (int)(Math.ceil(shortestTime)) ;
    }
    private int getMin(ArrayList<Integer> queue, double[] distTo)
    {
    	int currentMinimum = 0;
    	for(int x=0 ;x < queue.size(); x++)
    	{  		  			
    		if(distTo[queue.get(currentMinimum)] > distTo[queue.get(x)])
    		{
    			currentMinimum = x;
    		}
    	}
    	return currentMinimum;
    }

}