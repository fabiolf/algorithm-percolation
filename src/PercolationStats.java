import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] mTrials;
    private final double mMean;
    
    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();
        
        mTrials = new double[trials];
        
        for (int t = 0; t < trials; t++) {
            mTrials[t] = executeTrial(n);
//            System.out.println("t"+ t + " " + mTrials[t]);
//            System.out.print("\n");
            
        }
        
        mMean = StdStats.mean(mTrials);
        
    }

    private double executeTrial(int n) {
        // initialize percolation class (WUF under the hoods)
        Percolation perc = new Percolation(n);
        
        // initiate computation
        int maxSites = n*n;
        int openedSites = 0;
        
        while (!perc.percolates()) {
            int row = StdRandom.uniform(1, n+1);
            int col = StdRandom.uniform(1, n+1);
            if (!perc.isOpen(row, col)) {
//                System.out.print(".");
                perc.open(row, col);
                openedSites += 1;
            }
        }
        
        return ((double) openedSites/maxSites);
    }
    
    public double mean() {
        // sample mean of percolation threshold
        return mMean;
    }
    
    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(mTrials);
    }
    
    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return mMean-CONFIDENCE_95/Math.sqrt(mTrials.length); 
        
    }
    
    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mMean+CONFIDENCE_95/Math.sqrt(mTrials.length); 
    }

    public static void main(String[] args) {
        // test client (described below)
        if (args.length < 2) {
            System.out.println("not enough arguments (2 needed)");
            return;
        }
        
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
//        System.out.println("[DEBUG] n = " + n + " | trials = " + trials);
        
        PercolationStats percS = new PercolationStats(n, trials);
        
        System.out.println("mean                    = " + percS.mean());
        System.out.println("stddev                  = " + percS.stddev());
        System.out.println("95% confidence interval = [" + percS.confidenceLo() + ", " + percS.confidenceHi() + "]");
    }

}
