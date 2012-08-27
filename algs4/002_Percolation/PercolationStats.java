/*----------------------------------------------------------------
 *  Author:        Matt Farmer
 *  Written:       08/25/2012
 *  Last updated:  08/25/2012
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats
 *
 *  Tests the percolation as per the specification available at:
 *    http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 *----------------------------------------------------------------*/
public class PercolationStats {

    private int rowLen;
    private int total;
    private int runCount;
    private double[] results;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        rowLen = N;

        //-- Run the experiment T times
        int i = T;
        total = 0;
        runCount = 0;
        results = new double[T];
        while (0 < i--) {
            int c = monteCarloSimulation(N);
            results[i] = (double) c/N/N;
            total += c;
            runCount++;
        }
    }

    // run the Monte Carlo simulation on an N-by-N grid
    private int monteCarloSimulation(int N) {
        int c = 0;
        Percolation p = new Percolation(N);
        while (!p.percolates()) {
            int i = 1+StdRandom.uniform(N);
            int j = 1+StdRandom.uniform(N);
            if (!p.isOpen(i, j)) {
                c++;
                p.open(i, j);
            }
        }
        return c;
    }

    // sample mean of percolation threshold
    public double mean() {
        return (double) total/runCount/rowLen/rowLen;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (1 == runCount) {
            return Double.NaN;
        }
        return StdStats.stddev(results);
    }

    // test client, described at
    // http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int T = StdIn.readInt();
        PercolationStats ps = new PercolationStats(N, T);

        // Print out the results
        double m = ps.mean();
        double s = ps.stddev();
        double l = (m - (1.96*s)/Math.sqrt(T));
        double h = (m + (1.96*s)/Math.sqrt(T));

        StdOut.println("mean                    = "+ m);
        StdOut.println("stddev                  = "+ s);
        StdOut.println("95% confidence interval = "+ l +", "+ h);
    }

}
