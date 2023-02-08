package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PrincipalCliente {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub

		Socket socket= new Socket("127.0.0.1",7000);
		ConexionCliente conecta= new ConexionCliente(socket);
		
		conecta.start();
	}

}
