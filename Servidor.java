package validar_cpf_no_servidor.soquets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;

/**
 * @author Elvir Moraes 
 *  ______                                ______ 
 * |  C   |                              |  S   | 
 * |  l   |    -----> Informar CPF -->   |  e   | Validar( CPF ) 
 * |  i   |                              |  r   | - isCPF    ? 
 * |  e   |                              |  v   | - isValido ? 
 * |  n   |   <----- true or false <--   |  e   | 
 * |__t___|                              |__r___| 
 * 
 */
public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	public static void main(String[] args) {
		try {
			// Criar porta de recep˜˜o
			server = new ServerSocket(50000);
			socket = server.accept();
			// Criar os fluxos de entrada e sa˜da
			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());

			// Recebimento do valor String
			String valor = entrada.readUTF();

			// Processamento do valor
			boolean retorno = isCPF(valor);
			retorno = isValidCPF(valor);


			// Envio dos dados (resultado)
			saida.writeBoolean(retorno);

			socket.close();

		} catch (Exception e) {

		}

	}

	/**
	 * 
	 * 
	 * @param CPF
	 * @return
	 */
	public static boolean isCPF(String CPF) {

		System.out.println( "Entrada no isCPF: " + imprimeCPF( CPF ) );


		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (    CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))

			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {

				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ( ( dig10 == CPF.charAt( 9 ) ) && ( dig11 == CPF.charAt( 10 ) ) )
				return ( true );
			else
				return ( false );
		} catch ( InputMismatchException erro ) {

			return ( false );

		}


	}

	public static String imprimeCPF(String CPF) {
		return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." + CPF.substring(6, 9) + "-"
				+ CPF.substring(9, 11));
	}

	public static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static String padLeft(String text, char character) {
		return String.format("%11s", text).replace(' ', character);
	}

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	/**
	 * @param cpf
	 * @return true ou false
	 */
	public static boolean isValidCPF( String cpf ) {

		System.out.println( "Entrada no isValidCPF: " + imprimeCPF( cpf ) );

		cpf = cpf.trim().replace(".", "").replace("-", "");
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		for (int j = 0; j < 10; j++)
			if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
				return false;

		Integer digito1 = calcularDigito( cpf.substring(0, 9),           pesoCPF );
		Integer digito2 = calcularDigito( cpf.substring(0, 9) + digito1, pesoCPF );
		return cpf.equals( cpf.substring( 0, 9 ) + digito1.toString( ) + digito2.toString( ) );
	}

}
