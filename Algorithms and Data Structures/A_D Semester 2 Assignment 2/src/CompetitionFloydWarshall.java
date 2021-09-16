import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

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
 * This class implements the competition using Floyd-Warshall algorithm
 */

public class CompetitionFloydWarshall {

	private int sA;
	private int sB;
	private int sC;
	private Intersection[] cityIntersections;
	/**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     */
    CompetitionFloydWarshall (String filename, int sA, int sB, int sC)
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
    	
    	double minimumTime = -2;
    	int slowestSpeed = Math.min(sA, sB);
    	slowestSpeed = Math.min(slowestSpeed, sC);
    	if(slowestSpeed < 50 || slowestSpeed > 100)
    	{
    		return -1;
    	}
    	
    	double matrix[][] = makeGraphMatrix(this.cityIntersections);
    	
    	for(int x=0; x < matrix.length; x++)
    	{
    		for (int y=0; y < matrix.length; y++)
    		{
    			for(int z=0; z < matrix.length; z++)
    			{
    				if(matrix[y][x] + matrix[x][z] < matrix[y][z])
    					matrix[y][z] = matrix[y][x] + matrix[x][z];
    			}
    		}
    	}
    	
    	double maxPath = findMaxPath(matrix);
    	if(maxPath >= 9999999 || maxPath == -1)
    	{
    		return -1;
    	}
    	else
    	{
    		minimumTime = (maxPath*1000)/slowestSpeed;
    		return (int) (Math.ceil(minimumTime));
    	}
    		
    }
    
    private double[][] makeGraphMatrix(Intersection[] city)
    {
    	double matrix[][] = new double[city.length][city.length];

    	for(int x = 0; x < matrix.length; x++)
    	{
    		for(int y = 0;  y < matrix.length; y++)
    		{
    			if(x == y)
    				matrix[x][y] = 0;
    			else
    				matrix[x][y] = 10000000;
    		}
    	}
    	
    	for(int x = 0; x < matrix.length; x++)
    	{
    		int amountOfStreets = city[x].streets.size();
    		
    		for(int y = 0; y < amountOfStreets; y++)
    		{
    			matrix[x][city[x].streets.get(y).destination] = city[x].streets.get(y).distance;
    		}
    	}
    	return matrix;
    }
    private double findMaxPath(double matrix[][])
    {
    	double maxPath = -1;
    	for(int x = 0; x < matrix.length; x++ )
    	{
    		for(int y = 0; y < matrix.length; y++)
    		{
    			if(matrix[x][y]>maxPath)
    			{
    				maxPath = matrix[x][y];
    			}
    		}
    	}
    	return maxPath;
    }

}