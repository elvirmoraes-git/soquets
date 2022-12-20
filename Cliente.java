package validar_cpf_no_servidor.soquets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {

	private static Socket socket;
	private static DataInputStream entrada;
	private static DataOutputStream saida;

	public static String cpf = "";

	public static void main( String[] args ) {

		try {

			socket = new Socket( "127.0.0.1", 50000 );

			entrada = new DataInputStream ( socket.getInputStream ( ) );
			saida   = new DataOutputStream( socket.getOutputStream( ) );


			String valor = leitura( "Digite um CPF para verificação: " );

			//O valor é enviado ao servidor
			saida.writeUTF( valor );

			//Recebe-se o resultado do servidor
			boolean resultado = entrada.readBoolean( );

			//Mostra o resultado na tela
			if ( resultado )
				System.out.println( "Este CPF é válido   " );
			else
				System.out.println( "Este CPF é inválido " );

			socket.close();

		} catch( Exception e ) {

		}

	}


	/**
	 * @param mensagem = Exibir mensagem.
	 * @return entrada do usuário.
	 */
	public static String leitura ( String mensagem ) {

		//Recebe do usuário algum valor
		BufferedReader cd      = new BufferedReader( new InputStreamReader( System.in ) );
		String         retorno = "";

		if ( !mensagem.isEmpty( ) ) 
			System.out.print( mensagem );

		try {
			retorno = cd.readLine();
		} catch ( IOException e1 ) {
			// TODO Auto-generated catch block
			System.out.println( "Erro de entrada!" );
		}

		return retorno;
	}

}
