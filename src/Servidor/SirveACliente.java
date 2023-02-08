package Servidor;

import java.net.Socket;
import java.util.Iterator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SirveACliente extends Thread {

	int[][] enteros = new int[4][4];
	double[][] decimal = new double[3][3];
	private Socket socket;
	private DataInputStream escucha;
	private DataOutputStream escribe;

	private int ok = 100;
	private int fallo = 200;

	public SirveACliente(Socket socket, int[][] enteros, double[][] decimal) throws IOException {
		super();
		this.socket = socket;
		this.enteros = enteros;
		this.decimal = decimal;

		this.escucha = new DataInputStream(socket.getInputStream());
		this.escribe = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		do {
			int opcionElegida = 0;
			int eleccionMatriz = 0;
			int columna = 0;
			int fila = 0;

			try {
				/*
				 * esperamos la primera eleccion del cliente que tiene que ser 1 o 2 si no es
				 * asi, se quedara esperando en la eleccion enviaremos un ok al cliente o un
				 * fallo que usaremos para que el cliente pueda seguir pidiendo cosas
				 */
				opcionElegida = esperaOpcionElegida();
				/*
				 * si escoge la 1 que es recibir informacion, escuchamos de que matriz quiere
				 * recibir informacion
				 * 
				 */
				if (opcionElegida == 1) {

					eleccionMatriz = esperaEleccionMatriz();

					if (eleccionMatriz == 1) {

						// escuchamos la fila que quiere
						fila = esperaFilaMatriz1();

						// escuchamos la columna

						columna = esperaColumnaMatriz1();

						// enviamos la informacion que nos pide
						escribe.writeInt(enteros[fila - 1][columna - 1]);

					} else if (eleccionMatriz == 2) {

						// esperamos fila matriz 2
						fila = esperaFilaMatriz2();
						// escuchamos la columna de matriz 2
						columna = esperaColumnaMatriz2();

						// enviamos informacion pedida de matriz 2

						escribe.writeDouble(decimal[fila - 1][columna - 1]);

						// abajo se resetean valores

					}

				}
				// opcionElegida ==2 significa que vamos a modificar
				else if (opcionElegida == 2) {

					eleccionMatriz = esperaMatrizParaSetear();

					// si elige la matriz 1
					if (eleccionMatriz == 1) {
						// escuchamos la fila que quiere
						fila = esperaFilaMatriz1();

						// escuchamos la columna

						columna = esperaColumnaMatriz1();

						// escuchamos el valor lo seteamos y enviamos el ok
						try {
							int seteo = this.escucha.readInt();
							this.enteros[fila - 1][columna - 1] = seteo;
							this.escribe.writeInt(this.ok);

						} catch (Exception e) {

							this.escribe.writeInt(this.fallo);
						}

					}
					// si elige la matriz 2
					else if (eleccionMatriz == 2) {
						fila = esperaFilaMatriz2();
						// escuchamos la columna

						columna = esperaColumnaMatriz2();

						// escuchamos el valor lo seteamos y enviamos el ok

						double set = this.escucha.readDouble();
						decimal[fila - 1][columna - 1] = set;
						this.escribe.writeInt(this.ok);

					}

				}
				//reiniciamos valores a cero para siguiente bucle
				opcionElegida = 0;
				eleccionMatriz = 0;
				columna = 0;
				fila = 0;

			} catch (Exception e) {
				// TODO: handle exception
			}
		

		} while (true);
	}

	public int esperaMatrizParaSetear() throws IOException {
		int eleccionMatriz;
		do {

			eleccionMatriz = this.escucha.readInt();

			if (eleccionMatriz == 1 || eleccionMatriz == 2) {
				this.escribe.writeInt(ok);
			} else {
				this.escribe.writeInt(fallo);
			}
		} while (eleccionMatriz < 1 || eleccionMatriz > 2);
		return eleccionMatriz;
	}

	public int esperaColumnaMatriz2() throws IOException {
		int columna;
		do {
			
			columna = this.escucha.readInt();

			if (columna > 0 & columna <= 3) {
				this.escribe.writeInt(this.ok);
			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (columna <= 0 || columna >= 4);
		return columna;
	}

	public int esperaFilaMatriz2() throws IOException {
		int fila;
		do {

			fila = this.escucha.readInt();

			if (fila > 0 && fila <= 3) {
				this.escribe.writeInt(this.ok);
			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (fila <= 0 || fila >= 4);
		return fila;
	}

	public int esperaColumnaMatriz1() throws IOException {
		int columna;
		do {

			columna = this.escucha.readInt();

			if (columna >= 1 && columna <= 4) {
				this.escribe.writeInt(this.ok);
			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (columna <= 0 || columna >= 5);
		return columna;
	}

	public int esperaFilaMatriz1() throws IOException {
		int fila;
		do {

			fila = this.escucha.readInt();

			if (fila >= 1 && fila <= 4) {
				this.escribe.writeInt(this.ok);
			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (fila <= 0 || fila >= 5);
		return fila;
	}

	public int esperaEleccionMatriz() throws IOException {
		int eleccionMatriz;
		do {

			eleccionMatriz = this.escucha.readInt();

			if (eleccionMatriz == 1 || eleccionMatriz == 2) {
				this.escribe.writeInt(this.ok);
			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (eleccionMatriz < 1 || eleccionMatriz > 2);
		return eleccionMatriz;
	}

	public int esperaOpcionElegida() throws IOException {
		int opcionElegida;
		do {
			opcionElegida = this.escucha.readInt();

			if (opcionElegida == 1 || opcionElegida == 2) {
				this.escribe.writeInt(this.ok);

			} else {
				this.escribe.writeInt(this.fallo);
			}
		} while (opcionElegida != 1 && opcionElegida != 2);
		return opcionElegida;
	}

}
