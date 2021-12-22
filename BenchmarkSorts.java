/*
Author: Adrian Miller
Professor Didulo
CMSC 451: Design and analysis of Computer Algorithms
Project 1 : Benchmark Algorithm
Selected Algorithm: Mergesort

Classes/interfaces needed
BenchmarkSort
UnsortedException
SortInterface
MergeSort
*/

import java.io.*;

public class BenchmarkSorts {

    //Main method to run program 1
    public static void main(String[] args) throws UnsortedException, IOException {
        //Create Sort object
        SortInterface mySort = new MergeSort();
        int[] sizes = {100,200,300,400,500,600,700,800,900,1000};

        //Recursive solution
        FileWriter writer = new FileWriter("recursion_benchmark.txt");
        for(int i = 0; i < sizes.length; i++) {
            //Write size on file
            writer.write(sizes[i]+" ");
            //Loop 50 times to sort the same array
            for(int n = 1; n <= 50; n++) {
                
                //Generate random array between 1 and size
                int[] arr = new int[sizes[i]];
                for(int j = 0; j < arr.length; j++) {
                    arr[j] = (int)(Math.random()*sizes[i]+1);    
                }
                mySort.recursiveSort(arr);
                checkSort(arr);
                long time = mySort.getTime();
                int count = mySort.getCount();
                writer.write("("+time+","+count+") ");
            }
            writer.write("\n");
        }
        writer.close();

        //Iterative solution
        writer = new FileWriter("iterative_benchmark.txt");
        for(int i = 0; i < sizes.length; i++) {
            //Write size on file
            writer.write(sizes[i]+" ");
            //Loop 50 times to sort the same array
            for(int n = 1; n <= 50; n++) {
                //Generate random array between 1 and size
                int[] arr = new int[sizes[i]];
                for(int j = 0; j < arr.length; j++) {
                    arr[j] = (int)(Math.random()*sizes[i]+1);    
                }
                //Sort array, get results and print on file
                mySort.iterativeSort(arr);
                checkSort(arr);
                long time = mySort.getTime();
                int count = mySort.getCount();
                writer.write("("+time+","+count+") ");
            }
            writer.write("\n");
        }
        writer.close();
    }
    
    //Checks if the array is sorted. Throws an exception if not
    public static void checkSort(int[] arr) throws UnsortedException {
        for(int i = 1; i < arr.length; i++) {
            if(arr[i] < arr[i-1]) {
                throw new UnsortedException("Array is not sorted"); 
            }
        }
    }


    //Method that creates Dummy objects to warm up the JVM
    public static void warmUp() {
        for(int i = 0; i < 1000000; i++) {
            Dummy d = new Dummy();
            d.m();
        }
    }
  
    //Dummy code taken from:https://www.baeldung.com/java-jvm warmup
    private static class Dummy {
        public void m() {} 
    }

}

// Purpose: To handle unsorted arrays or other invalid input
//exception is thrown when array is unsorted
 class UnsortedException extends Exception {
  
  public UnsortedException(String msg) {
        super("Unsorted exception: "+msg);
    }
  
}

 interface SortInterface {
  
    public void recursiveSort(int[] list);
    
  public void iterativeSort(int[] list);
  
    public int getCount();
  
    public long getTime();
}


 class MergeSort  implements SortInterface {
  
    private int count;
    private long time;
  
    public MergeSort() {
      count = 0;
        time = 0;
    }
    
    //Method that sorts the input array recursively taken and adjuste from:  https://www.geeksforgeeks.org/merge-sort/
    public void recursiveSort(int[] list) {
        long start = System.nanoTime();
        count = 0;
      recursiveSort(list, 0, list.length-1);
        time = System.nanoTime() - start;
    }
  
    //Method that sorts the input array iteratively sited from: https://www.techiedelight.com/iterative-merge-sort-algorithm-bottom-up/
    public void iterativeSort(int[] list) {
        long start = System.nanoTime();
        count = 0;
        //Initialize left and right indices
        int l = 0;
        int r = list.length - 1;
  
        // Divide the array into blocks of size m
        // m = [1, 2, 4, 8, 16...]
        for (int m = 1; m <= r - l; m = 2*m) {
            // for m = 1, i = 0, 2, 4, 6, 8...
            // for m = 2, i = 0, 4, 8, 12...
            // for m = 4, i = 0, 8, 16...
            // ...
            //[0m 1][2m 3][4  5][
            //[5, 2, 7, 8, 9, 1, 3, 6, 3]
              
            //[0  m     3][  
            //[2, 5, 7, 8, 1, 9, 3, 6, 3]
            
            //[0,       m           r] []
            //[2, 5, 7, 8, 1, 3, 6, 9, 3]
              
            //[0,                   m  r]
            //[1, 2, 3, 5, 6, 7, 8, 9, 3]            
            for (int i = l; i < r; i += 2*m) {
                int from = i;
                int mid = i + m - 1;
                //Find right index as the possible right (considering full block) and the max right at all
                int to = Integer.min(i + 2 * m - 1, r);
        //Merge regions sorted
                merge(list, from, mid, to);
            }
        }
        time = System.nanoTime() - start;
    }
  
    //Method that sorts the input array within the given region recursively
    public void recursiveSort(int[] list, int l, int r) {
        if(l < r) {
            //Find middle index
            int mid = (r+l)/2;
            //Sort the left region
            recursiveSort(list, l, mid);
            //Sort the right region
            recursiveSort(list, mid+1, r);
            //Merge both sorted regions
            merge(list, l, mid, r);
        }
    }  
  
    //Method that merges both regions defined on list
    public void merge(int[] list, int l, int mid, int r) {
      //Helper array to store both regions together
        int[] helperArr = new int[r+1];
        for(int i = l; i <= r; i++) {
          helperArr[i] = list[i];
        }
        //Merge both regions
        int i = l;      //index for left region
        int j = mid+1;  //index for right region
        for(int k = l; k <= r; k++) {
          if(i <= mid && j <= r) {
                //Means both regions have values to check
                count++; //one comparison made
                if(helperArr[i] < helperArr[j]) {
                    //Value on left region is less
                  list[k] = helperArr[i];
                    i++;
                } else {
                    //Value on right region is less
                  list[k] = helperArr[j];
                    j++;
                }   
            } else if(i <= mid) {
              //Means only left region has values to consider
              list[k] = helperArr[i];
                i++; 
            } else {
                //Means only right region has values to consider
              list[k] = helperArr[j];
                j++;
            }    
        }
    }
  
  //Getter for time
    public long getTime() {
      return time;
    }
  
    //Getter for count
    public int getCount() {
        return count;
    }
}