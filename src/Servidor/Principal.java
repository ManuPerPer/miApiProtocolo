package Servidor;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Principal {
	
	

	public static void main(String[] args) {
		//variables
		ServerSocket socket;
		int[][] enteros = new int[4][4];
		double [][] decimal= new double[3][3];
		
		cargaMatrizes(enteros, decimal); 
		
		try {
			 
			 socket= new ServerSocket(7000);
			//lanzo primero mi propio servicio para poder usar la consola
			SirveAConsola meSirvo= new SirveAConsola(enteros, decimal);
			meSirvo.start();
			System.out.println("Esperando que alguien se conecte");
			
			//pongo el servidor a la espera de que algun cliente se conecte
			Socket socket_cli = socket.accept();	
			System.out.println("Cliente conectado");
			
			//una vez conectado, lanzo su hilo
			SirveACliente cliente= new SirveACliente(socket_cli, enteros, decimal);
				cliente.start();

		 }catch (Exception e) {
			// TODO: handle exception
		}
		 
		 
		 
		 
		
		 
		
	}

	public static void cargaMatrizes(int[][] enteros, double[][] decimal) {
		Random r= new Random();
		
		//rellenamos arrays
		 for (int i = 0; i < enteros.length; i++) {
			 for (int j = 0; j < enteros[i].length; j++) {
				enteros[i][j]= r.nextInt(9)+1;
				
				
			}
			
			
		}
		 
	
		 for (int i = 0; i < decimal.length; i++) {
			 for (int j = 0; j < decimal.length; j++) {
				decimal[i][j]=Double.valueOf(r.nextInt(101)+1)/100;
				;
			}
		
		}
	}

}
