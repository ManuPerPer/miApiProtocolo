package Servidor;

import java.util.Scanner;

public class SirveAConsola  extends Thread{
	
	int[][] enteros = new int[4][4];
	double [][] decimal= new double[3][3];
	
	
	public SirveAConsola(int[][] enteros, double[][] decimal) {
		super();
		
		this.enteros=enteros;
		this.decimal= decimal;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		int eleccion=0;
		Scanner sc= new Scanner(System.in);
		do {
			do {
				System.out.println("QUE ARRAY QUIERES VER");
				System.out.println("1- ENTEROS");
				System.out.println("2- DECIMALES");
				System.out.println("PULSE 1/2");
				eleccion = sc.nextInt();
			} while (eleccion!=1 && eleccion!=2);
			
			if(eleccion==1) {
				 muestraArray1();
			}
			else if(eleccion==2) {
				 muestraArray2();
				
			}
			 System.out.println("\n***************************\n");
			eleccion=0;
			
			
			
			
		}while(true);
	}


	public void muestraArray2() {
		for (int i = 0; i < decimal.length; i++) {
			 for (int j = 0; j < decimal.length; j++) {
				System.out.print(decimal[i][j]+"\t");
			}
			 System.out.println();
		}
	}


	public void muestraArray1() {
		for (int i = 0; i < enteros.length; i++) {
			 for (int j = 0; j < enteros[i].length; j++) {
				System.out.print(enteros[i][j]+"\t");
				
			}
			 System.out.println();
			
		}
	}
	
	

}
