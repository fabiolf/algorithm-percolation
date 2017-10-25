import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF mUf;
    private final int mSize;
    private boolean[] mBoolGrid;
    private int mOpenSites = 0;
    private boolean mBottomConnected = false;
//    private boolean mPercolates = false;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // creates a WQU data structure with two virtual nodes
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
            
        mUf = new WeightedQuickUnionUF(n*n+2);

        mSize = n;
        mBoolGrid = new boolean[n*n+2];
        mBoolGrid[0] = true;
        mBoolGrid[n*n+1] = true;
    }
    
    // translates a 2 dimensional position to a 1 dimensional position in an array
    private int ijto1d(int i, int j) {
        return ((i*mSize-mSize) + j);
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > mSize || col <= 0 || col > mSize)
            throw new java.lang.IllegalArgumentException();
        // sets m_bool_grid[position] to true
        // checks whether any neighbors are true and connect them
        int elementIndex = ijto1d(row, col); 
        if (!mBoolGrid[elementIndex]) {
            mBoolGrid[elementIndex] = true;
            mOpenSites += 1;
            
            // connects to the top virtual element
            if (row == 1)
                mUf.union(0, elementIndex);

            // check the element above
            if (row > 1  && mBoolGrid[elementIndex-mSize])
                mUf.union(elementIndex, elementIndex-mSize);

            // check the element below
            if (row < mSize && mBoolGrid[elementIndex+mSize])
                mUf.union(elementIndex, elementIndex+mSize);

            // check the element at the right
            if (col < mSize && mBoolGrid[elementIndex+1])
                mUf.union(elementIndex, elementIndex+1);
            
            // check the element at the left
            if (col > 1 && col <= mSize && mBoolGrid[elementIndex-1])
                mUf.union(elementIndex, elementIndex-1);
            
            // connects to the bottom virtual element
            if (row == mSize && !mBottomConnected && isFull(row, col)) {
                mBottomConnected = true;
                mUf.union(mSize*mSize+1, elementIndex);
            }
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > mSize || col <= 0 || col > mSize)
            throw new java.lang.IllegalArgumentException();
        return (mBoolGrid[ijto1d(row, col)]);
    }
    
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        boolean isConnected = mUf.connected(ijto1d(row, col), 0);
        return (isOpen(row, col) && isConnected);
    }
    
    // number of open sites
    public int numberOfOpenSites() {
        return mOpenSites;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return mUf.connected(0, mSize*mSize+1);
    }
    
    public void printGrid() {
        for (int i = 1; i <= mSize; i++) {
            String line = "";
            for (int j = 1; j <= mSize; j++) {
                line += (mBoolGrid[ijto1d(i, j)] ? "O" : "X");
                line += "  ";
            }
            System.out.println(line);
        }
        System.out.println("Open sites: " + numberOfOpenSites());
        System.out.println("Percolates? " + percolates());
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation per1 = new Percolation(3);
        System.out.println("All sites blocked");
        per1.printGrid();
//        System.out.println("Adding 1 openings");
        per1.open(1, 3);
        per1.open(2, 3);
        per1.open(3, 3);
        per1.printGrid();
    }
}
