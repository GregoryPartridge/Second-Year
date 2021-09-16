import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.junit.Assert;
import java.util.Arrays;

//-------------------------------------------------------------------------
/**
 *  Test class for SortComparison.java
 *
 *  @author
 *  @version HT 2020
 */
@RunWith(JUnit4.class)
public class SortComparisonTest
{
    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
        new SortComparison();
    }

    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Check that the methods work for empty arrays
     */
    @Test
    public void testEmpty()
    {
    }


    // TODO: add more tests here. Each line of code and each decision in Collinear.java should
    // be executed at least once from at least one test.
    
    @Test
	public void testInsertionSort()
	{
    	Assert.assertTrue(Arrays.equals(new double[] {-4,2,3,4,11,17}, SortComparison.insertionSort(new double[] {4,2,11,17,3,-4})));
	}

	@Test
	public void testSelectionSort()
	{
    	Assert.assertTrue(Arrays.equals(new double[] {-4,2,3,4,11,17}, SortComparison.selectionSort(new double[] {4,2,11,17,3,-4})));
	}

	@Test
	public void testQuickSort()
	{		
		Assert.assertTrue(Arrays.equals(new double[] {-4,2,3,4,11,17}, SortComparison.quickSort(new double[] {4,2,11,17,3,-4})));
	}

	@Test
	public void testMergeSortRecursive()
	{		
		Assert.assertTrue(Arrays.equals(new double[] {-4,2,3,4,11,17}, SortComparison.mergeSortRecursive(new double[] {4,2,11,17,3,-4})));
	}

	@Test
	public void testMergeSortIterative()
	{
		double[] a = SortComparison.mergeSortIterative(new double[] {4,2,11,17,3,-4});
		
		for(int i = 0; i < a.length; i++)
		{
			System.out.print(" ,"+a[i]);
		}
		
		Assert.assertTrue(Arrays.equals(new double[] {-4,2,3,4,11,17}, SortComparison.mergeSortIterative(new double[] {4,2,11,17,3,-4})));
	}
    // ----------------------------------------------------------
    /**
     *  Main Method.
     *  Use this main method to create the experiments needed to answer the experimental performance questions of this assignment.
     *
     */
    public static void main(String[] args)
    {
        //TODO: implement this method
    }
}
