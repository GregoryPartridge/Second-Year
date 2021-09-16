
public class SortComparison {


	static double[] insertionSort (double a[])
	{
		double temp;
		for( int i = 0; i < a.length; i++)
		{
			for(int j = i; j > 0; j--)
			{
				if(a[j] < a[j - 1])
				{
					temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;
				}
			}
		}
		return a;
	}

	static double[] selectionSort (double a[])
	{
		for(int i = 0; i < a.length - 1; i++)
		{
			int min_idx = i;
			for(int j = i + 1; j < a.length; j++)
			{
				if(a[j] < a[min_idx])
				{
					min_idx = j;
				}
			}
			double temp = a[min_idx];
			a[min_idx] = a[i];
			a[i] = temp;			
		}
		
		return a;
	}
	
	static double[] quickSort (double a[])
	{
		quickSort(a, 0, a.length-1);
		return a;
	}
	
	private static void quickSort(double[] a,int low,int  high)
	{
	    if (low < high)
	    {
	        /* pi is partitioning index, arr[pi] is now
	           at right place */
	        int pi = partition(a, low, high);

	        quickSort(a, low, pi - 1);  // Before pi
	        quickSort(a, pi + 1, high); // After pi
	    }
	}
	
	private static int partition (double a[], int low, int high)
	{
	    // pivot (Element to be placed at right position)
	    double pivot = a[high];  
	 
	    int i = (low - 1);  // Index of smaller element

	    for (int j = low; j <= high- 1; j++)
	    {
	        // If current element is smaller than the pivot
	        if (a[j] < pivot)
	        {
	            i++;
	            swap(a, i, j);
	        }
	    }
		swap(a, i+1, high);
	    return (i + 1);
	}
	
	private static void swap(double[] a, int index1, int index2)
	{
		double temp = a[index1];
		a[index1] = a[index2];
		a[index2] = temp;
	}
	
	static double[] mergeSortRecursive (double a[])
	{
		double[] aCopy = new double[a.length];
		sort(a, 0, a.length-1); 
		
		return a;
	}
	
	 private static void sort(double arr[], int l, int r) 
	    { 
	        if (l < r) 
	        { 
	            int m = (l+r)/2; 
	  
	            sort(arr, l, m); 
	            sort(arr , m+1, r); 
	  
	            merge(arr, l, m, r); 
	        } 
	    } 
	
	 private static void merge(double[] arr, int l, int m, int r) 
	    { 
	        int n1 = m - l + 1; 
	        int n2 = r - m; 
	  
	        double L[] = new double [n1]; 
	        double R[] = new double [n2]; 
	  
	        for (int i=0; i<n1; ++i) 
	            L[i] = arr[l + i]; 
	        for (int j=0; j<n2; ++j) 
	            R[j] = arr[m + 1+ j];
	        
	        int i = 0, j = 0; 
	        int k = l; 
	        while (i < n1 && j < n2) 
	        { 
	            if (L[i] <= R[j]) 
	            { 
	                arr[k] = L[i]; 
	                i++; 
	            } 
	            else
	            { 
	                arr[k] = R[j]; 
	                j++; 
	            } 
	            k++; 
	        } 
	        while (i < n1) 
	        { 
	            arr[k] = L[i]; 
	            i++; 
	            k++; 
	        } 
	        while (j < n2) 
	        { 
	            arr[k] = R[j]; 
	            j++; 
	            k++; 
	        } 
	    } 

	public static double[] mergeSortIterative (double a[])
	{
		mergeSort(a, 0, a.length - 1); 
		return a;
	}
	
	private static void mergeSort(double arr[], int l, int r) 
	{ 
	   if (l < r) 
	   { 
	      int m = l+(r-l)/2;
	      mergeSort(arr, l, m); 
	      mergeSort(arr, m+1, r); 
	      merge(arr, l, m, r); 
	   } 
	} 
	
}
