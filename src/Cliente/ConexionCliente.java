package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;



public class ConexionCliente extends Thread {
	private Socket socket;

	private DataInputStream escucha;
	private DataOutputStream escribe;
	private int okEleccion = 0;
	private int okMatriz = 0;
	private int okFila = 0;
	private int okColumna = 0;
	private int seteoCorrecto = 0;

	public ConexionCliente(Socket socket) throws IOException {
		super();
		this.socket = socket;

		this.escucha = new DataInputStream(socket.getInputStream());
		this.escribe = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		//scaner para numeros
		Scanner sc = new Scanner(System.in);
		//scaner para strings
		Scanner sc2 = new Scanner(System.in);
		do {

			int eleccion = 0;
			int matriz = 0;
			int fila = 0;
			int columna = 0;

			eleccion = pideEleccion(sc);
			if (eleccion == 1) {
				//preguntamos sobre que matriz quiere informacion
				matriz = preguntaMatriz(sc);
				// pedimos fila y columna en matriz1
				if (matriz == 1) {
					//pedimos fila y columna
					fila = pideFila(sc);
					columna = pideColumna(sc);	
					// mostramos resultado que devuelve el servidor
					try {
						System.out.println("El numero que has pedido es: " + this.escucha.readInt());
						System.out.println();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//si matriz es 2
				else if (matriz == 2) {
					//pedimos fila y columna
					fila = pideFila(sc);
					columna = pideColumna(sc);
					// mostramos por consola la devolucion del servidor
					try {
						System.out.println("El numero que has pedido es: " + this.escucha.readDouble());
						System.out.println();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				//ponemos todos los ok a 0 para el siguiente bucle
				this.okColumna = 0;
				this.okEleccion = 0;
				this.okFila = 0;
				this.okColumna = 0;
				this.seteoCorrecto=0;


			}
			
			// si eleccion es 2 queremos setear informacion
			else if (eleccion == 2) {
				//preguntamos que matriz quiere modificar
				matriz = preguntaMatrizParaSetear(sc);

				if (matriz == 1) {
					//pedimos fila y columna para setear
					fila = pideFilaParaSetear(sc);
					columna = pideColumnaParaSetear(sc);
					
					/*pedimos valor como string, si el metodo esNumero devuelve true
					 * lo convertimos a entero( por que estamos en la matriz de enteros)
					 * se lo enviamos al servidor y esperamos su respuesta de ok
					 * si ha ido bien devuelve 100 y se imprime por consola el mensaje deque todo ha ido bien
					 * 
					 * 
					 */
					try {		
						String valor="";
						do {
							System.out.println("QUE VALOR QUIERES DARLE AL ARRAY 1 EN SUS POSICIONES [" + (fila - 1) + "]["
									+ (columna - 1) + "]?");
							valor=sc2.nextLine();
						}while(esNumero(valor)==false);
						
						int numero = Integer.parseInt(valor);
						this.escribe.writeInt(numero);
						int seteo = this.escucha.readInt();
						if (seteo == 100) {
							System.out.println("Se ha cambiado el valor correctamente");

						} else {
							System.out.println("Error inesperado");
						}

					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// trabajaremos la matriz2
				else if (matriz == 2) {
					//pedimos fila y columna para modificar 
					fila = pideFilaParaSetear(sc);
					columna = pideColumnaParaSetear(sc);
					/*
					 * pedimos un numero como string, se lo pasamos al metodo esDecimal, si devuelve true,
					 * es que ese string se puede castear a double, una vez que tenemos un numero valido para double
					 * (por que estamos trabajando la matriz de decimales)
					 * se lo pasamos al servidor y esperamos su respuesta de ok. si todo ha ido bien imprime por pantalla que
					 * todo ha ido correcto. 
					 */
					try {
						String valor="";
						do {
							System.out.println("QUE VALOR QUIERES DARLE AL ARRAY 2 EN SUS POSICIONES [" + (fila - 1) + "]["
									+ (columna - 1) + "]?");
							valor=sc2.nextLine();
						}while(esDecimal(valor)==false);
				

						double numero = Double.parseDouble(valor);
						numero=Math.round(numero*100.0)/100.0;
							this.escribe.writeDouble(numero);
							this.seteoCorrecto=this.escucha.readInt();
							if (this.seteoCorrecto == 100) {
								System.out.println("Se ha cambiado el valor correctamente");
								
							} else {
								System.out.println("Error inesperado");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					
				}

				// ponemos todo a 0 para el siguiente bucle
				this.okColumna = 0;
				this.okEleccion = 0;
				this.okFila = 0;
				this.okColumna = 0;
				this.seteoCorrecto=0;
			}

		} while (true);

	}
	
	/*
	 * metodo el cual le paso un string, se intenta castear, si esto ocurre ese string se puede convertir en entero,
	 * el metodo devuelve true ( que es un entero)
	 * si da fallo en el casteo devolvera false( no es un numero entero)
	 */
	public boolean esNumero(String valor) {
		boolean es=false;
		try {
			int numero=Integer.parseInt(valor);
			es= true;
		}catch (Exception e) {
			// si falla el casteo es sigue siendo false
		}
		
		return es;
	}

	/*
	 * metodo el cual le paso un string, lo intenta castear, si esto ocurre ese string se puede convertir en double
	 * y devolvera que es un decimal
	 * si no devolvera false
	 * 
	 */
	public boolean esDecimal(String numero) {
		boolean es = false;
		try {
			double n = Double.parseDouble(numero);
			es = true;
		} catch (Exception e) {
			// es sigue siendo false

		}
		return es;
	}


	/*
	 * nuestro servidor esta a la espera de un numero de columna. este metodo esta encapsulado en un do while
	 * se quedara pidiendo numero de fila hasta que el servidor nos devuelva codigo 100 ( correcto)
	 * como las columnas dependiendo si estamos preguntando sobre matriz 1 o 2 son diferentes, el servidor 
	 * esta configurado para saber en que matriz va a trabajar. asique de tal manera segun la matriz que eligamos
	 * dara por buenas 3 o 4 columnas
	 * 
	 */

	public int pideColumna(Scanner sc) throws NumberFormatException {

		int columna = 0;

		do {
			System.out.println("DE QUE COLUMNA QUIERES VER??");
			try {
				columna = sc.nextInt();
				this.escribe.writeInt(columna);
				this.okColumna = escucha.readInt();
				if (this.okColumna == 200) {
					System.out.println("Elige una columna valida");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}while (this.okColumna == 0 || this.okColumna == 200);
		return columna;
	}
	
	/*
	 * metodo igual que pideColumna pero este nos indica que la accion que estamos haciendo sera modificar la columna elegida
	 */

	public int pideColumnaParaSetear(Scanner sc) throws NumberFormatException {

		int columna = 0;

		do {
			System.out.println("DE QUE COLUMNA QUIERES MODIFICAR??");
			try {
				columna = sc.nextInt();
				this.escribe.writeInt(columna);
				this.okColumna = escucha.readInt();
				if (this.okColumna == 200) {
					System.out.println("Elige una columna valida");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}while (this.okColumna == 0 || this.okColumna == 200);
		return columna;
	}
	
	/*
	 * este metodo nos pide una fila, como esta encapsulado en un do while, podemos enviar el numero que queramos
	 * el servirod nos dira si es correcto o no ( para matriz 1 esta configurado que acepte hasta fila4 y para matriz 2 solo
	 * hasta fila 3
	 * una vez enviaemos la fila que queremos ver, nos devolvera 100 si es valida o 200 si es erronea,
	 * este metodo se quedara preguntando hasta que el servidor no nos de una respuesta de 100.
	 * de igual manera se quedara esperando una respuesta que considera valida
	 */

	public int pideFila(Scanner sc) throws NumberFormatException {
		int fila = 0;

		do{
			System.out.println("DE QUE FILA QUIERE VER??");

			try {
				fila = sc.nextInt();
				this.escribe.writeInt(fila);
				this.okFila = this.escucha.readInt();
				if (this.okFila == 200) {
					System.out.println("Elige una fila valida");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block

			}

		}while (this.okFila == 0 || this.okFila == 200) ;
		return fila;
	}
	/*
	 * metodo igual que pideFila pero que nos pregunta que fila queremos modificar
	 */

	public int pideFilaParaSetear(Scanner sc) throws NumberFormatException {
		int fila = 0;

		do{
			System.out.println("DE QUE FILA MODIFICAR??");

			try {
				fila = sc.nextInt();
				this.escribe.writeInt(fila);
				this.okFila = this.escucha.readInt();

				if (this.okFila == 200) {
					System.out.println("Elige una fila valida");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block

			}

		}while (this.okFila == 0 || this.okFila == 200);
		return fila;
	}
	/**
	 * como nuestro servidor esta esperando un numero, esta configurado para que solo pueda ser 1 o 2 lo que espera.
	 * nuestro metodo esta encapsulado en un do while. enviara eleccion ( el numero que sea)
	 * si el servidor lo da por bueno ( 1 o 2) devolvera un 100 si no enviara 200
	 * nuestro metodo solo saldra del do while mientras que this.okEleccion= 100. de
	 * igual manera el servidor hasta que no llega un 1 o 2 enviara codigo 200 y seguira a la espera
	 * 
	 */
	public int pideEleccion(Scanner sc) throws NumberFormatException {
		int elec = 0;

		do {
			System.out.println("QUE QUIERES HACER??");
			System.out.println("1- RECIBIR");
			System.out.println("2- ENVIAR");
			System.out.println("PULSE 1/2");

			try {
				elec = sc.nextInt();
				this.escribe.writeInt(elec);
				this.okEleccion = this.escucha.readInt();
			} catch (IOException e) {

			}

		} while (this.okEleccion == 0 || this.okEleccion == 200);
		return elec;
	}

	/**
	 * De igual manera que pideEleccion, el servidor estara a la espera. podemos enviar el numero que queramos, pero
	 * el servidor solo nos dara por bueno el 1 o 2.
	 * nuestro metodo se quedara en el do while mientras que this.okMatriz no sea 100 ( que es el codigo que devuelve el servidor)
	 * cuando envio un numero valido, de igual manera el servidor se quedara a la espera de que envie otro numero hasta que le de uno
	 * valido
	 * 
	 */
	public int preguntaMatriz(Scanner sc) throws NumberFormatException {

		int matriz = 0;
		do {

			System.out.println("QUE MATRIZ QUIERES VER??");
			System.out.println("1- MATRIZ DE ENTEROS");
			System.out.println("2- MATRIZ DE DECIMALES");
			System.out.println("PULSE 1/2");

			try {
				matriz = sc.nextInt();
				this.escribe.writeInt(matriz);
				this.okMatriz = this.escucha.readInt();
				if (this.okMatriz == 200) {
					System.out.println("Debe elegir entre 1 o 2");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (this.okMatriz != 100);
		return matriz;
	}
	/*
	 * este metodo es exactamente igual que el pide matriz solo que aqui ya nos indica por consola que la accion que vamos
	 * a realizar es una modificacion sobre la matriz que elijamos
	 */

	public int preguntaMatrizParaSetear(Scanner sc) throws NumberFormatException {

		int matriz = 0;
		do {

			System.out.println("QUE MATRIZ QUIERES MODIFICAR??");
			System.out.println("1- MATRIZ DE ENTEROS");
			System.out.println("2- MATRIZ DE DECIMALES");
			System.out.println("PULSE 1/2");

			try {
				matriz = sc.nextInt();
				this.escribe.writeInt(matriz);
				this.okMatriz = this.escucha.readInt();
				if (this.okMatriz == 200) {
					System.out.println("Debe elegir entre 1 o 2");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (this.okMatriz != 100);
		return matriz;
	}

}
