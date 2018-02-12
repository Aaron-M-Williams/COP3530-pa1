public class MatrixElement{
    //Row, Column, and Value ints are declared.
    private int row;
    private int column;
    private int value;

    //Constructor to create new element, consists of row, column, and value. This is essentially every spot in the
    //matrix that is not zero.
    public MatrixElement(int row, int column, int value){
        this.row=row;
        this.column = column;
        this.value = value;
    }
    //Function to get the value.
    public int getValue(){
        return this.value;
    }
    //Function to get the column
    public int getColumn(){
        return this.column;
    }
    //function to get the row
    public int getRow(){
        return this.row;
    }

    //Overrides the built in equals method.
    //An element is equal if it is in the same row and column.
    @Override
    public boolean equals(Object ME){
        return (this.row==((MatrixElement)ME).row && this.column==((MatrixElement)ME).column);
    }
}