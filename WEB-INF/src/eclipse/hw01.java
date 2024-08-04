package eclipse;

public class hw01 {
	
	public static void main(String[] argv) {
		
		int[][] x = { { 5, 1, 4 }, { 2, 7, 2 }, { 8, 1, 3 } };
		int[][] y = { { 1, 3, 4 }, { 2, 7, 2 }, { 8, 1, 3 } };
		int[][] z = null;
		
		z = GTR(x,y);
		
		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[i].length; j++) {				
				System.out.print(String.format("z[%d][%d]: %d, ", i, j, z[i][j]));
			}
			System.out.println("");
		}
		

		//System.out.println(add(1, 3));
	}

	public static int[][] GTR(int[][] x,int[][] y){   
			 
		int[][] z = new int[x.length][x[0].length];

		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[i].length; j++) {
				z[i][j] = x[i][j] + y[i][j];
				//System.out.println(String.format("x[%d][%d]: %d", i, j, x[i][j]));
			}
		}
		
		return z;
	}

	public static int add(int x, int y) {
		return (x + y);
	}
}