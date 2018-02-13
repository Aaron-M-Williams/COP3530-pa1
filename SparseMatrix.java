import java.util.Iterator;
import java.util.LinkedList;


class SparseMatrix implements SparseInterface
{

    //Variable Declaration for the list that will contain
    //the matrix and the variable that will hold the maxsize.
    private int maxSize;
    //Declares the LinkedList which holds MatrixElement nodes.
    private LinkedList<MatrixElement> matrix;


    //Basic constructor which creates a new matrix of size 5.
    public SparseMatrix(){
        this.matrix= new LinkedList<MatrixElement>();
        this.maxSize=5;

    }

    //constructor used for the minor method to create a matrix
    //of a different size than the default constructor.
    public SparseMatrix(int size, LinkedList<MatrixElement> minorMatrix){
        this.maxSize = size;
        this.matrix = minorMatrix;

    }


    //Clear function. It "erases" the matrix by starting a new list
    //and assigning it to the matrix variable
    public void clear(){
        this.matrix = new LinkedList<MatrixElement>();
        return;
    }


    //Function for adjusting the size of the matrix. Taking in
    //the new size as an integer.
    public void setSize(int size){
        //Checks if the size is negative. If it is, there is an error
        //prompt printed out, and nothing is changed.
        if (size < 0)
        {
            System.out.println("Error! Size cannot be negative. Nothing has been changed, try again.");
            return;
        }

        this.maxSize=size;
        matrix.clear();
        return;

    }

    //Function for adding an element into the matrix. It takes
    //in the row, column and data to set the correct spot in the
    //matrix to the value passed in.
    public void addElement(int row, int col, int data){
        //If the value passed in is out of the size allowed by max size,
        //or it is negative, then an error is presented and nothing is added.
        if (col >=this.maxSize || row >= this.maxSize || row<0 || col<0){
            System.out.println("Invalid row / column position. The element will not be added.");
            return;
        }
        //Removes the element at the current spot using the
        //removeElement function. Passes in the col and row values
        //from the function parameters.
        removeElement(row, col);

        //Adds the value by creating a new MatrixElement with the
        //row, col, and data passed in.
        matrix.add(new MatrixElement(row, col, data));
        return;
    }

    //Function to remove element from the list. It uses the built in remove function by creating an object to compare against.
    public void removeElement(int row, int col){
        //Checks that the row / col combination is valid and prints out an error if the combination is invalid.
        if (col >=this.maxSize || row >= this.maxSize || row<0 || col<0){
            System.out.println("Invalid row / column position. The element will not be removed.");
            return;
        }
        //Invokes the built in remove function.
        matrix.remove(new MatrixElement(row, col, 1));


    }


    //Function to retrieve an element from the list. It takes in the row and column and returns the
    //value at that object in the list.
    public int getElement(int row, int col){
        //Checks to see if the row / col combination is valid. Prints out an error if the combination is invalid.
        if (col >=this.maxSize || row >= this.maxSize || row<0 || col<0){
            System.out.println("Invalid row / column position. Please try again.");
            return 0;
        }
        //Utilizes the indexOf function to find the position of the element in the list.
        int pos = matrix.indexOf(new MatrixElement(row, col, 0));
        //If the position is -1 ie the element is not in the list, it returns 0.
        if(pos ==-1)
            return 0;
        //Returns the value at the position which was found earlier.
        int value = ((matrix.get(pos)).getValue());
        return value;

    }


    //Function for finding the determinant.
    public int determinant(){
        //Base Cases, if maxSize==1, then the determinant is the single value.
        if (maxSize ==1) {
            return matrix.get(0).getValue();
        }
        //Base Case(2), if maxSize==2, then the determinant is simple to calculate. If the element doesn't exist,
        //the getElement function will return 0 for that element (sparse matrix).
        if (maxSize ==2){
            return getElement(0,0)*getElement(1,1)-getElement(0,1)*getElement(1,0);
        }
        //Recursive case.
        int answer = 0;

        //Shrinks the matrix into recursive steps using minor function which shrinks it by one row / column at a time
        //until the base case is hit.
        for (int j=0; j<maxSize;j++){
            answer += Math.pow(-1,j)*getElement(0,j)*minor(0,j).determinant();
        }
        return answer;
    }


    //Method for finding the minor of the matrix.
    public SparseInterface minor(int row, int col) {
        //Method for trying the row and column. If they're out of the size expected, then there is an error thrown.
        //Error is caught in the catch, and the error is printed out.
        try {
            if (col >= this.maxSize || row >= this.maxSize || row < 0 || col < 0)
                throw new IndexOutOfBoundsException("Error. Invalid row / column combination.");
            }
        catch (IndexOutOfBoundsException e) {
            System.out.println(e.toString());
        }
        //New size of one less than the previous.
        int sizeNew = maxSize - 1;
        //New linked list of matrix elements.
        LinkedList<MatrixElement> tempList = new LinkedList<MatrixElement>();
        //New iterator to go through the original matrix.
        Iterator matrixIterator = matrix.iterator();

        //While iterator has next, copy data from original list to new list, sans the row / column we are
        //getting rid rid of.
        while (matrixIterator.hasNext()) {
            MatrixElement value = (MatrixElement) matrixIterator.next();
            if (value.getRow() != row && value.getColumn() != col) {

                if (value.getRow() > row && value.getColumn() > col) {
                    tempList.add(new MatrixElement(value.getRow() - 1, value.getColumn() - 1, value.getValue()));
                }
                else if (value.getRow() > row) {
                    tempList.add(new MatrixElement(value.getRow() - 1, value.getColumn(), value.getValue()));
                }
                else if (value.getColumn() > col) {
                    tempList.add(new MatrixElement(value.getRow(), value.getColumn() - 1, value.getValue()));
                }
                else {
                    tempList.add(value);
                }
            }
        }
        //returns new SparseMatrix of new size and list.
        return new SparseMatrix(sizeNew, tempList);
    }


    //Function for printing the matrix to a string.
    public String toString(){
        //Creates new 2D array, which the values from the list will be added to.
        //Java by default initializes the array values to 0.
        int[][] multi = new int[maxSize][maxSize];
        int location=0;
        //tempNumber is equal to the size of the matrix list. Used for while loop.
        int tempNumber=matrix.size();
        //New string builder, more efficient than typical string.
        StringBuilder string = new StringBuilder();
        StringBuilder test= new StringBuilder();

        //while temp number is greater than 0. Import the values from the list to the array.
        while(tempNumber>0){
            //Try to import, but if the row / col are invalid, throw an error.
            try {
                if (matrix.get(location).getColumn()>=maxSize || matrix.get(location).getRow()>=maxSize){
                    throw new IndexOutOfBoundsException("The size of the matrix has been changed, please clear the matrix and try again");

                }
                //The array's position is set to the current matrix element's row and column, and the array
                //value is set to the value.
                multi[matrix.get(location).getColumn()][matrix.get(location).getRow()] = matrix.get(location).getValue();
                //Decrement tempNumber (number of elements in the list.
                location++;
                tempNumber--;
            }
            //catch returns error when the matrix is invalid.
            catch (IndexOutOfBoundsException e){
                System.out.println(e.toString());
                return "Error, please clear the matrix and try again.";
            }

        }
        //Loops through array to print out the row / column and data of entries in the array.
        //If there is nothing present, then it returns an empty string.
        test.append("");
        for(int i =0; i<maxSize; i++)
        {
            for (int j = 0; j < maxSize; j++)
            {
                if (multi[j][i] != 0) {
                    test.append(i + " " + j + " " + multi[j][i]+"\n");
                }
            }
        }


        //FOR TESTING! After the array has been filled. This prints it out using two for loops to go through both dimensions.
        //Uses StringBuilder to save complexity.
        for(int i =0; i<maxSize;i++){
            for(int j=0; j<maxSize;j++){
                string = string.append(multi[j][i]);
                string = string.append(" ");
            }
            string = string.append("\n");
        }
        //System.out.println(string.toString());
        return (test.toString());


    }


    //Get size returns the current maxSize of the matrix.
    public int getSize(){
        return (this.maxSize);

    }
}