package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.dao.SequencialDAO;
import org.example.entidade.Sequencial;
import org.hibernate.Session;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.text.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class CassUtil {

	/**
	 * Calcula os anos, meses e dias entre duas datas
	 * @param dataInicial data Calendar
	 * @param dataFinal data Calendar
	 * @return um String no seguinte formato: 00 anos, 00 meses e 00 dias
	 */
	public static String getAnosMesesDiasEntreDatasEmString(Calendar dataInicial, Calendar dataFinal) {
		try {
			LocalDate data1 = LocalDate.of(dataInicial.get(Calendar.YEAR), dataInicial.get(Calendar.MONTH)+1, dataInicial.get(Calendar.DAY_OF_MONTH));
			LocalDate data2 = LocalDate.of(dataFinal.get(Calendar.YEAR), dataFinal.get(Calendar.MONTH)+1, dataFinal.get(Calendar.DAY_OF_MONTH));

			Period period = Period.between(data1, data2);
			return period.getYears() + " anos " + period.getMonths() + " meses e " + period.getDays() +" dias";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Calcula os anos, meses e dias entre duas datas
	 * @param dataInicial data Calendar
	 * @param dataFinal data Calendar
	 * @return uma lista de inteiros na seguinte ordem: posição 0 os anos, 1 os meses e 2 os dias
	 */
	public static List<Integer> getAnosMesesDiasEntreDatas(Calendar dataInicial, Calendar dataFinal) {
		try {
			LocalDate data1 = LocalDate.of(dataInicial.get(Calendar.YEAR), dataInicial.get(Calendar.MONTH)+1, dataInicial.get(Calendar.DAY_OF_MONTH));
			LocalDate data2 = LocalDate.of(dataFinal.get(Calendar.YEAR), dataFinal.get(Calendar.MONTH)+1, dataFinal.get(Calendar.DAY_OF_MONTH));

			Period period = Period.between(data1, data2);
			List<Integer> periodo = Arrays.asList(period.getYears(), period.getMonths(), period.getDays());

			return periodo;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Cálcula o dígito verificado de 2 dígitos para número de 8 caracteres.
	 * @param NUM um número String de 8 caracteres
	 * @return Um String de 2 dígitos. Ex.: 00, ou, NULL caso haja erro;
	 */
	public static String getDigitosVerificador(String NUM, int tamanhoNUM) {

		if (NUM == null || NUM.isEmpty() || NUM.length() != tamanhoNUM) return null;

		// Verifica se todos os números são iguais. Ex.: 1111
		if (StringUtils.countMatches(NUM, NUM.substring(0, 1)) == tamanhoNUM) return null;

		char dig1, dig2;
		int sm, i, r, num, peso;

		try {
			// Calculo do 1º Digito Verificador
			sm = 0;
			peso = 10;
			for (i=0; i<tamanhoNUM; i++) {
				// Se não for número retorna null
				if (!Character.isDigit(NUM.charAt(i))) return null;

				// converte o i-esimo caractere do NUMERO em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 e a posicao de '0' na tabela ASCII)
				num = NUM.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig1 = '0';
			} else {
				dig1 = (char)(r + 48); // converte no respectivo caractere numerico
			}

			// Calculo do 2º Digito Verificador
			sm = 0;
			peso = 11;
			for(i=0; i<tamanhoNUM; i++) {
				num = NUM.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig2 = '0';
			} else {
				dig2 = (char)(r + 48);
			}

			return dig1 +""+ dig2;

		} catch (InputMismatchException erro) {
			return null;
		}
	}

	public static String getProximoNumero(Session session, String nome) {
		SequencialDAO sequencialDAO = new SequencialDAO(session);
		Sequencial sequencial = sequencialDAO.getSequencialPorNome(nome);
		if (sequencial == null) {
			sequencial = new Sequencial(nome);
			sequencialDAO.salvar(sequencial);
		}
		String numero = sequencial.getProxNumero();
		sequencialDAO.atualizar(sequencial);

		return numero;
	}

	public static String getProximoNumeroIniciadoComAno(Session session, String nome, int tamanho) {
		SequencialDAO sequencialDAO = new SequencialDAO(session);
		Sequencial sequencial = sequencialDAO.getSequencialPorNome(nome);
		if (sequencial == null) {
			sequencial = new Sequencial(nome);
			sequencialDAO.salvar(sequencial);
		}
		String numero = Calendar.getInstance().get(Calendar.YEAR) + sequencial.getProxNumero(tamanho-4);
		sequencialDAO.atualizar(sequencial);

		return numero;
	}

	public static String getProximoNumero(Session session, String nome, int tamanho) {
		SequencialDAO sequencialDAO = new SequencialDAO(session);
		Sequencial sequencial = sequencialDAO.getSequencialPorNome(nome);
		if (sequencial == null) {
			sequencial = new Sequencial(nome);
			sequencialDAO.salvar(sequencial);
		}
		String numero = sequencial.getProxNumero(tamanho);
		sequencialDAO.atualizar(sequencial);

		return numero;
	}

	/**
	 * Encontrada em 10/12/2017 às 09h40min
	 * http://www.sanfoundry.com/java-program-generate-all-possible-combinations-given-list-numbers/
	 * Alterada em 10/12/2017 às 09h50min
	 * Alterada por Cass
	 * @param num número, no formato array de inteiros, a partir do qual deve ser gerada as sequências.
	 * @param numDigitos quantidade de dígitos que cada sequência deve ter.
	 * @param sequencias lista de sequências geradas, não haverá na lista retornada.
	 */
	public static void gerarCombinacoes(int[] num, int numDigitos, Set<String> sequencias) {
		String s = "";
		if (numDigitos == num.length) {
			for (int i = 0; i < num.length; i++) {
				s += num[i];
			}
			sequencias.add(s);
		} else {
			for (int i = numDigitos; i < num.length; i++) {
				int temp = num[numDigitos];
				num[numDigitos] = num[i];
				num[i] = temp;

				gerarCombinacoes(num, numDigitos + 1, sequencias);

				temp = num[numDigitos];
				num[numDigitos] = num[i];
				num[i] = temp;
			}
		}
	}

//	public static List<Integer> getUnidadesSubordinadas(Session session, Unidade unidade) {
//
//		List<Integer> unidades = new ArrayList<Integer>();
//		// INICIO DA ARVORE: UNIDADE PAI
//		unidades.add(unidade.getIdUnidade());
//
//		// ADICIONA OS FILHO QUE SAO PAI
//		if (unidade.getUnidadesSubordinadas().size() > 0) {
//			// CHAMA A FUNCAO RECURSIVA
//			getSubUnidades(unidades, unidade);
//		}
//
//		return unidades;
//	}
//
//	protected static boolean getSubUnidades(List<Integer> unidades, Unidade unidade) {
//
//		for (Unidade un : unidade.getUnidadesSubordinadas()) {
//
//			// ADICIONA O FILHO QUE E PAI
//			unidades.add(un.getIdUnidade());
//
//			// ADICIONA OS NETOS QUE TEM FILHO
//			if (un.getUnidadesSubordinadas().size() > 0)
//				getSubUnidades(unidades, un);
//		}
//
//		return true;
//	}

	public static String getNumeroStringComZerosEsquerda(String numero, int totalDigitos) {
		while (numero.length() < totalDigitos) {
			numero = "0" + numero;
		}

		return numero;
	}

//	public static String getProximoNumero(Session session, String nome) {
//		SequencialDAO sequencialDAO = new SequencialDAO(session);
//		Sequencial sequencial = sequencialDAO.getSequencialPorNome(nome);
//		if (sequencial == null) {
//			sequencial = new Sequencial(nome);
//			sequencialDAO.salvar(sequencial);
//		}
//		String numero = sequencial.getProxNumero();
//		sequencialDAO.atualizar(sequencial);
//
//		return numero;
//	}

	public static String converterBigDecimalParaNumeroStringPtBr(BigDecimal valor, String formato) {
		formato = formato == null || formato.isEmpty() ? "#,###,##0.00" : formato;
		DecimalFormat df = new DecimalFormat(formato);

		return df.format(valor);
	}

	public static String converterBigDecimalParaNumeroStringPtBr(BigDecimal valor) {
		if (valor == null) return "";
		DecimalFormat df =
				new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(new Locale("pt","BR")));

		return df.format(valor);
	}

	public static BigDecimal converterNumeroStringPtBrParaBigDecimal(String numeroStringPtBr) {
		if (numeroStringPtBr == null || numeroStringPtBr.isEmpty())
			return null;

		numeroStringPtBr = numeroStringPtBr.replaceAll("\\.", "");
		numeroStringPtBr = numeroStringPtBr.replaceAll("\\,", "\\.");

		if (numeroStringPtBr == null || !Pattern.matches("\\d+(\\.\\d+)?", numeroStringPtBr))
			return null;

		return new BigDecimal(numeroStringPtBr);
	}

	/**Converte um número inteiro para Algarismo Romano
	 @param numero numero a ser convertido
	 */
	public static String converterDecimalRomano(int numero) {
		if (numero <=0) return "";
		// storing roman values of digits from 0-9
		// when placed at different places
		String[] m = {"", "M", "MM", "MMM"};
		String[] c = {"", "C", "CC", "CCC", "CD", "D",
				"DC", "DCC", "DCCC", "CM"};
		String[] x = {"", "X", "XX", "XXX", "XL", "L",
				"LX", "LXX", "LXXX", "XC"};
		String[] i = {"", "I", "II", "III", "IV", "V",
				"VI", "VII", "VIII", "IX"};

		// Converting to roman
		String milhar = m[numero/1000];
		String centena = c[(numero%1000)/100];
		String dezena = x[(numero%100)/10];
		String unidade = i[numero%10];

		String romano = milhar + centena + dezena + unidade;

		return romano;
	}

	/**Converte número em Algarismo Romano para Decimal
	 @param texto algarismo romano para ser convertido
	 */
	public static int converterRomanoDecimal(String texto) {
		int n = 0;
		int numeralDaDireita = 0;
		for (int i = texto.length() - 1; i >= 0; i--) {
			int valor = (int) traduzirNumeralRomano(texto.charAt(i));
			n += valor * Math.signum(valor + 0.5 - numeralDaDireita);
			numeralDaDireita = valor;
		}
		return n;
	}
	//Método Auxiliar para conversão do número romano para decimal.
	private static double traduzirNumeralRomano(char caractere) {
		return Math.floor(Math.pow(10, "IXCM".indexOf(caractere))) + 5 * Math.floor(Math.pow(10, "VLD".indexOf(caractere)));
	}

	/**Método para conversão de número decimal para por extenso
	Possíveis retornos
 	@param vlr valor a ser convertido
	@param tipoString 0 - padrão, retorna sem nenhuma terminação
				1 - retorna o valor em reais
				2 - retorna o valor em porcentagem
				3 - retorna o valor em graus
	 */
	public static String converterNumeroPorExtenso(double vlr, String tipoString) {
		int tipo = 0;
		switch (tipoString) {
			case "real": case "reais": case "R$":
				tipo = 1;
				break;
			case "porcentagem": case "percent": case "%":
				tipo = 2;
				break;
			case "grau": case "graus": case "º":
				tipo = 3;
				break;

		}
		if (vlr == 0) {
			switch (tipo) {
				case 1:
					return "zero reais";
				case 3:
					return "zero graus";
				default:
					return "zero";
			}
		}
		long inteiro = (long)Math.abs(vlr); // parte inteira do valor
		double resto = vlr - inteiro;       // parte fracionária do valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15)
			return("Erro: valor superior a 999 trilhões.");

		String s = "", saux, vlrP;
		String centavos = String.valueOf((int)Math.round(resto * 100));

		String[] unidade = {"", "um", "dois", "três", "quatro", "cinco",
				"seis", "sete", "oito", "nove", "dez", "onze",
				"doze", "treze", "quatorze", "quinze", "dezesseis",
				"dezessete", "dezoito", "dezenove"};
		String[] dezena = {"", "", "vinte", "trinta", "quarenta", "cinquenta",
				"sessenta", "setenta", "oitenta", "noventa"};
		String[] centena = {"", "cento", "duzentos", "trezentos",
				"quatrocentos", "quinhentos", "seiscentos",
				"setecentos", "oitocentos", "novecentos"};
		String[] qualificaS = {"", "mil", "milhão", "bilhão", "trilhão"};
		String[] qualificaP = {"", "mil", "milhões", "bilhões", "trilhões"};

// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = 0;
		boolean umReal = false, tem = false;
		while (!vlrS.equals("0")) {
			tam = vlrS.length();
// retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
// 1a. parte = 789 (centena)
// 2a. parte = 456 (mil)
// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam-3, tam);
				vlrS = vlrS.substring(0, tam-3);
			}
			else { // última parte do valor
				vlrP = vlrS;
				vlrS = "0";
			}
			if (!vlrP.equals("000")) {
				saux = "";
				if (vlrP.equals("100"))
					saux = "cem";
				else {
					n = Integer.parseInt(vlrP, 10);  // para n = 371, tem-se:
					cent = n / 100;                  // cent = 3 (centena trezentos)
					dez = (n % 100) / 10;            // dez  = 7 (dezena setenta)
					unid = (n % 100) % 10;           // unid = 1 (unidade um)
					if (cent != 0)
						saux = centena[cent];
					if ((n % 100) <= 19) {
						if (saux.length() != 0)
							saux = saux + " e " + unidade[n % 100];
						else saux = unidade[n % 100];
					}
					else {
						if (saux.length() != 0)
							saux = saux + " e " + dezena[dez];
						else saux = dezena[dez];
						if (unid != 0) {
							if (saux.length() != 0)
								saux = saux + " e " + unidade[unid];
							else saux = unidade[unid];
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == 0) // 1a. parte do valor (um real)
						umReal = true;
					else saux = saux + " " + qualificaS[i];
				}
				else if (i != 0)
					saux = saux + " " + qualificaP[i];
				if (s.length() != 0)
					s = saux + ", " + s;
				else s = saux;
			}
			if (((i == 0) || (i == 1)) && s.length() != 0)
				tem = true; // tem centena ou mil no valor
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
		}

		if (s.length() != 0) {
			if (umReal)
				switch (tipo) {
					case 1: s = s + " real";
						break;
					case 2: s = s + " porcento";
						break;
					case 3: s = s + " grau";
						break;
					default: break;
				}

			else if (tem)
				switch (tipo) {
					case 1: s = s + " reais";
						break;
					case 2:
						if (!centavos.equals("0")) {
							break;
						}
						s = s + " porcento";
						break;
					case 3:
						if (!centavos.equals("0")) {
							break;
						}
						s = s + " graus";
						break;
					default: break;
				}
			else {
				switch (tipo) {
					case 1: s = s + " de reais";
						break;
					case 2:
						if (!centavos.equals("0")) {
							break;
						}
						s = s + " porcento";
						break;
					case 3:
						if (!centavos.equals("0")) {
							break;
						}
						s = s + " de graus";
						break;
					default: break;
				}
			}
		}

// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != 0) // se não é valor somente com centavos
				switch (tipo) {
					case 1: s = s + " e ";
						break;
					default: s = s + " vírgula ";
				}
			if (centavos.equals("1"))
				switch (tipo) {
					case 1: s = s + "um centavo";
						break;
					case 2: s = s + "um porcento";
						break;
					case 3: s = s + "um grau";
						break;
					default: break;
				}
			else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19)
					s = s + unidade[n];
				else {             // para n = 37, tem-se:
					unid = n % 10;   // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10;    // dez  = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != 0)
						s = s + " e " + unidade[unid];
				}
				switch (tipo) {
					case 1: s = s + " centavos";
						break;
					case 2: s = s + " porcento";
						break;
					case 3: s = s + " graus";
						break;
					default: break;
				}
			}
		}
		return(s);
	}
	//método adicional caso nao passe o tipo
	public static String converterNumeroPorExtenso(double vlr) {
		return converterNumeroPorExtenso(vlr, "0");
	}

	public static String padronizarNomeVariavelSoLetras(String str) {
		str = substituirCaracteresEspeciais(str);
		String[] array = str.toLowerCase().split(" ");
		String result = array[0];
		for (int i = 1; i < array.length; i++) {
			result += array[i].substring(0, 1).toUpperCase() + array[i].substring(1);
		}

		return result;
	}

	//Converte apenas a primeira letra em maiúscula da String
	public static String converterPrimeiraLetraMaiuscula(String str) {
		char[] arr = str.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		for (int i = 1 ; i < arr.length; i++) {
			arr[i] = Character.toLowerCase(arr[i]);
		}
		return new String(arr);
	}

	//Converte a primeira letra em maiúscula de todas as palavras da String
	public static String converterPrimeiraLetraMaiusculaNomeProprio(String str) {
		String[] nome = str.split(" ");
		StringBuilder retorno = new StringBuilder();
		for (int i = 0 ; i < nome.length; i++) {
			if (nome[i].length() > 3) {
				nome[i] = converterPrimeiraLetraMaiuscula(nome[i]);
				retorno.append(nome[i]);
				retorno.append(" ");
			} else {
				retorno.append(nome[i].toLowerCase());
				retorno.append(" ");
			}
		}
		return retorno.toString();
	}

	public static String substituirCaracteresEspeciais(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public static String UTF8toISO(String str){
        Charset utf8charset = StandardCharsets.UTF_8;
        Charset iso88591charset = StandardCharsets.ISO_8859_1;

        ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());

        // decode UTF-8
        CharBuffer data = utf8charset.decode(inputBuffer);

        // encode ISO-8559-1
        ByteBuffer outputBuffer = iso88591charset.encode(data);
        byte[] outputData = outputBuffer.array();

        return new String(outputData);
    }

	public static String ISOtoUTF8(String str){
        Charset utf8charset = StandardCharsets.UTF_8;
        Charset iso88591charset = StandardCharsets.ISO_8859_1;

        ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());

        // decode ISO-8559-1
        CharBuffer data = iso88591charset.decode(inputBuffer);

        // encode UTF-8
        ByteBuffer outputBuffer = utf8charset.encode(data);
        byte[] outputData = outputBuffer.array();

        return new String(outputData);
    }

	public static LocalDate converterDataStringParaLocalDate(String data) {
		if (Objects.isNull(data) || data.isEmpty())
			return null;
		String pattern = data.contains("-") ? "yyyy-MM-dd" : "dd/MM/yyyy";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(data, formatter);
	}

	private String md5(String senha) throws NoSuchAlgorithmException {
		MessageDigest m=MessageDigest.getInstance("MD5");
		m.update(senha.getBytes(),0,senha.length());
		return new BigInteger(1,m.digest()).toString(16);
	}
	public static String criptografar(String senha) {
		BigInteger hash = null;
		try  {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(senha.getBytes());
			hash = new BigInteger(1, md.digest());
			String senh = hash.toString(16);
			while (senh.length() < 32) {
				senh = "0" + senh;
			}
			return senh;

		} catch(NoSuchAlgorithmException ns) {
			ns.printStackTrace();
			return senha;
		}
	}

	public static String criptografar256(String frase, int tamanho) {
		try {
			if (tamanho > 64 || tamanho <= 0 ) {
				tamanho = 64;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(frase.getBytes());
			byte[] bytes = md.digest();

			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
				int parteBaixa = bytes[i] & 0xf;
				if (parteAlta == 0) s.append('0');
				s.append(Integer.toHexString(parteAlta | parteBaixa));
			}

			return s.substring(0, tamanho);

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String mascarar(String valor, String mascara) {
		StringBuilder dado = new StringBuilder();
		// remove caracteres não numéricos
		for (int i = 0; i < valor.length(); i++) {
			char c = valor.charAt(i);
			if (Character.isDigit(c)) {
				dado.append(c);
			}
		}

		int indMascara = mascara.length();
		int indCampo = dado.length();

		for (; indCampo > 0 && indMascara > 0; )
		{
			if (mascara.charAt(--indMascara) == '#')
				indCampo--;
		}
		int indMascara2=indMascara;
		StringBuilder saida = new StringBuilder();
		for (; indMascara2 < mascara.length(); indMascara2++)
			saida.append((mascara.charAt(indMascara2) == '#') ? dado.charAt(indCampo++) : mascara.charAt(indMascara2));

		return saida.toString();
	}

	public static String removerMascara(String valor) {
		if (valor == null || valor.trim().isEmpty())
			return null;
		valor = valor.replaceAll("\\.", "");
		valor = valor.replaceAll("\\/", "");
		valor = valor.replaceAll("\\-", "");
		valor = valor.replaceAll("\\(", "");
		valor = valor.replaceAll("\\)", "");

		return valor;
	}

	public static String getCepFormatado(String cep) {
		return cep == null || cep.isEmpty() ? "" : mascarar(cep, "##.###-###");
	}

	public static String getCNPJFormatado(String cnpj) {
		return cnpj == null || cnpj.isEmpty() ? "" : mascarar(cnpj, "##.###.###/####-##");
	}

	public static String getCPFFormatado(String cpf) {
		return cpf == null || cpf.isEmpty() ? "" :  mascarar(cpf, "###.###.###-##");
	}

	public static String getDataPorFormato(Calendar data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataFormatoFinalDocumento(Calendar data) {
		return data == null ? "" : data.get(Calendar.DAY_OF_MONTH) + " de " + getNomeMes(data.get(Calendar.MONDAY)+1, false, false) + " de " + data.get(Calendar.YEAR);
	}

	public static String getDataSoNumero(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataHoraSoNumero(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataHoraSoNumeroIniciaComAno(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataFormatada(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return data == null ? "" : sdf.format(data.getTime());
	}

	/**
	 *
	 * @param data
	 * @return Data formatada no formato: Dia-da-semana, 01 de Janeiro de 1900
	 */
	public static String getDataDiaFormatada(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEEEE',' dd 'de' MMMM 'de' yyyy");
		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataFormatadaUSA(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataHoraFormatadaUSA(Calendar dataHora) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (dataHora == null) return "";

		String dh = sdf.format(dataHora.getTime());
		dh = dh.replace(" ", "T");
		return dh;
	}

	public static String getDataHoraFormatadaPorFormato(Calendar data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getDataHoraFormatada(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		return data == null ? "" : sdf.format(data.getTime());
	}

	public static String getHoraFormatada(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return data == null ? "" : sdf.format(data);
	}

	public static String getHoraFormatada(Long data) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return data == null ? "" : sdf.format(data);
	}

	public static String getHoraFormatada(Time hora) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return hora == null ? "" : sdf.format(hora.getTime());
	}

	public static String getHoraMinutoFormatada(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return data == null ? "" : sdf.format(data.getTime());
	}

	public static Integer getHoraInteiro(Calendar data) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		return Integer.valueOf(data == null ? "" : sdf.format((data.getTime())));
	}

	public static Calendar getDataHoraIncrementada(Calendar data, int dias) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());
		dt.add(Calendar.DATE, dias);

		return dt;
	}

	public static Calendar getDataIncrementadaMeses(Calendar data, int meses) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());
		dt.add(Calendar.MONTH, meses);

		return dt;
	}

	public static Calendar getDataIncrementadaAnos(Calendar data, int anos) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());
		dt.add(Calendar.YEAR, anos);

		return dt;
	}

	public static Calendar getDataHoraIncrementaHora(Calendar data, int horas) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());
		dt.add(Calendar.HOUR, horas);

		return dt;
	}

	public static Calendar getDataHoraIncrementaMinuto(Calendar data, int minutos) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());
		dt.add(Calendar.MINUTE, minutos);

		return dt;
	}

	public static Calendar getDataUltimoMinutoDoDia(Calendar data) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());

		dt.set(Calendar.HOUR_OF_DAY, 23);
		dt.set(Calendar.MINUTE, 59);
		dt.set(Calendar.SECOND, 59);
		dt.set(Calendar.MILLISECOND, 0);

		return dt;
	}

	public static Calendar getDataHoraIncrementadaComHoraZerada(Calendar data, int dias) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(data.getTime());

		dt.add(Calendar.DATE, dias);
		dt.set(Calendar.HOUR_OF_DAY, 0);
		dt.set(Calendar.MINUTE, 0);
		dt.set(Calendar.SECOND, 0);
		dt.set(Calendar.MILLISECOND, 0);

		return dt;
	}

	public static Calendar converterDataStringParaCalendar(String data) {
		if (data == null || data.isEmpty())
			return null;

		SimpleDateFormat sdf;

		if (data.contains("-")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}

		Calendar dt = Calendar.getInstance();
		try {
			dt.setTime(sdf.parse(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dt;
	}

	public static Calendar converterDataUSAStringParaCalendar(String data) {
		if (data == null || data.isEmpty())
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dt = Calendar.getInstance();
		try {
			dt.setTime(sdf.parse(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dt;
	}

	public static Calendar converterDataHoraStringParaCalendar(String dataHora) {
		if (dataHora == null || dataHora.isEmpty())
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		if (dataHora.contains("-")) {
			dataHora = dataHora.replace("T", " ");
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		Calendar dh = Calendar.getInstance();
		try {
			dh.setTime(sdf.parse(dataHora));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dh;
	}

	public static Calendar converterDataHoraUSAStringParaCalendar(String dataHora) throws ParseException {
		if (dataHora == null || dataHora.isEmpty())
			return null;
		dataHora = dataHora.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar dh = Calendar.getInstance();
		dh.setTime(sdf.parse(dataHora));

		return dh;
	}

	public static Calendar getDataAtualComHoraZerada() {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);

		return data;
	}

	public static Calendar getDataComHoraZerada(Calendar data) {
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);

		return data;
	}

	public static Calendar getDataComMesIncrementado(Calendar data, int meses) {
		data.add(Calendar.MONTH, meses);

		return data;
	}

	public static String getSaudacao() {
		Integer hora = CassUtil.getHoraInteiro(Calendar.getInstance());

		if (hora >= 0 && hora <= 5) return "Boa madrugada";
		if (hora >= 5 && hora <= 11) return "Bom dia";
		if (hora >= 12 && hora <= 17) return "Boa tarde";
		if (hora >= 18 && hora <= 23) return "Boa noite";
		else
			return "Bem vindo";
	}

	public static String getTelefoneFormatado(String telefone) {
		if (telefone == null || telefone.length() == 0) return "";
		String mascara = telefone.length() == 10 ? "(##)####-####" : "(##)#####-####";
		String tel = "("+ mascarar(telefone, mascara);
		return tel;
	}

	public static String getDiaSemanaPorCalendar(Calendar c) {
		int dia = c.get(Calendar.DAY_OF_WEEK);
		switch(dia) {
			case 1: return "Domingo";
			case 2: return "Segunda";
			case 3: return "Terça";
			case 4: return "Quarta";
			case 5: return "Quinta";
			case 6: return "Sexta";
			case 7: return "Sábado";
			default : return "";
		}
	}

	public static Calendar getUltimoDiaMes(Calendar c) {
		Calendar ultimo = Calendar.getInstance();
		ultimo.setTime(c.getTime());
		ultimo.add(Calendar.MONTH, 1);
		ultimo.add(Calendar.DAY_OF_MONTH, -ultimo.get(Calendar.DAY_OF_MONTH));
		ultimo = CassUtil.getDataUltimoMinutoDoDia(ultimo);
		return ultimo;
	}

	public static Calendar getPrimeiroDiaMes(Calendar c) {
		Calendar primeiro = Calendar.getInstance();
		primeiro.setTime(c.getTime());
		primeiro.add(Calendar.DAY_OF_MONTH, -c.get(Calendar.DAY_OF_MONTH));
		primeiro.add(Calendar.DAY_OF_MONTH, 1);
		return primeiro;
	}

	public static Calendar getPrimeiroDiaAno(Calendar c) {
		Calendar primeiro = Calendar.getInstance();
		primeiro.setTime(c.getTime());
		primeiro.set(Calendar.MONTH, Calendar.JANUARY);
		primeiro.set(Calendar.DAY_OF_MONTH, 1);
		return primeiro;
	}

	public static Calendar getPrimeiroDiaAno(Integer ano) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.MONTH, Calendar.JANUARY);
		data.set(Calendar.DAY_OF_MONTH, 1);
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);
		return data;
	}

	public static Calendar getUltimoDiaAno(Calendar c) {
		Calendar ultimo = Calendar.getInstance();
		ultimo.setTime(c.getTime());
		ultimo.set(Calendar.MONTH, Calendar.DECEMBER);
		ultimo.set(Calendar.DAY_OF_MONTH, 31);
		return ultimo;
	}

	public static Calendar getUltimoDiaAno(Integer ano) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.MONTH, Calendar.DECEMBER);
		data.set(Calendar.DAY_OF_MONTH, 31);
		data.set(Calendar.HOUR_OF_DAY, 23);
		data.set(Calendar.MINUTE, 59);
		data.set(Calendar.SECOND, 59);
		data.set(Calendar.MILLISECOND, 0);
		return data;
	}

	public static boolean eAnoBissexto(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
	}

	@SuppressWarnings("unused")
	private static Calendar getPascoa(int year) {
        int C, G, H, I, J, L, EasterMonth, EasterDay;
        G = year%19 ;

        I = (19*G + 15) % 30 ;
        J = (year + (year/ 4) + I) % 7 ;

        C = year/ 100;
        H = (C - C/4 - ((8*C+13)/ 25) + 19*G + 15) % 30 ;
        I = H - (H / 28) * (1 - (H/28) * (29/(H+1)) * ((21-G)/11) ) ;
        J = (year + (year/ 4) + I + 2 - C + C/4) % 7 ;

        L = I - J ;
        EasterMonth = 3 + ((L + 40) / 44) ;
        EasterDay = L + 28 - 31 * (EasterMonth/ 4) ;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, EasterDay);
        cal.set(Calendar.MONTH, EasterMonth-1);
        cal.set(Calendar.YEAR, year);

        return getDataComHoraZerada(cal);
    }

	public static String getProximaMatricula(Session session, Calendar dataIngresso) {
		int anoIngresso = dataIngresso.get(Calendar.YEAR);
		int mesIngresso = dataIngresso.get(Calendar.MONTH)+1;
		String matricula = "Matricula " + anoIngresso + mesIngresso;
		SequencialDAO sequencialDAO = new SequencialDAO(session);
		Sequencial sequencial = sequencialDAO.getSequencialPorNome(matricula);
		if (sequencial == null) {
			sequencial = new Sequencial(matricula);
			sequencialDAO.salvar(sequencial);
		}

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());

		Integer proxMatricula = sequencial.getProxNumeroInteger();
		proxMatricula += 1;

		sequencial.setProxNumero(proxMatricula);
		sequencialDAO.atualizar(sequencial);

		String num = proxMatricula.toString();

		switch (num.length()) {
			case 1:num = "000"+num;break;
			case 2:num = "00"+num;break;
			case 3:num = "0"+num;break;
		}
		String msIngresso = String.valueOf(mesIngresso);
		msIngresso = msIngresso.length() < 2 ? "0"+msIngresso : msIngresso;

		num = anoIngresso + msIngresso + num;
		num += getDigitosVerificador(num, 10);

		return num;
	}

	/**
	 * Metodo que retorna um Blob passando um File
	 * @param f
	 * @return
	 * @throws SerialException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static Blob getBlobFromFile(File f) throws SerialException, SQLException, IOException {
		Blob b = new SerialBlob(Files.readAllBytes(f.toPath()));
		return b;
	}

	public static byte[] getBytesFromBlob(Blob b) throws SQLException {
		return b.getBytes( 1, ( int ) b.length() );
	}

	/**
	 * <p> Método que devolve e cria um arquivo em um local específico passado como parâmetro </p>
	 * @param b
	 * @param caminhoArquivo
	 * @return File
	 * @throws IOException
	 * @throws SQLException
	 */
	public static File getFileFromBlob(Blob b, String caminhoArquivo) throws IOException, SQLException {
		File f = new File(caminhoArquivo);
		FileUtils.writeByteArrayToFile(f, getBytesFromBlob(b));
		return f;
	}

	public static Calendar getDataHojeMenos2diasUteis() {
		Calendar hoje = Calendar.getInstance();
		Calendar doisDiasUteisAntes;
		int diaDaSemana = hoje.get(Calendar.DAY_OF_WEEK);

		if (diaDaSemana == Calendar.TUESDAY) { // TERCA
			doisDiasUteisAntes = getDataHoraIncrementadaComHoraZerada(hoje, -4);

		} else if (diaDaSemana == Calendar.MONDAY) { // SEGUNDA
				doisDiasUteisAntes = getDataHoraIncrementadaComHoraZerada(hoje, -4);

		} else if (diaDaSemana == Calendar.SUNDAY) { // DOMINGO
			doisDiasUteisAntes = getDataHoraIncrementadaComHoraZerada(hoje, -3);

		} else if (diaDaSemana == Calendar.SATURDAY) { // SABADO
			doisDiasUteisAntes = getDataHoraIncrementadaComHoraZerada(hoje, -2);

		} else {
			doisDiasUteisAntes = getDataHoraIncrementadaComHoraZerada(hoje, -2);
		}

		CassUtil.getDataComHoraZerada(doisDiasUteisAntes);

		return doisDiasUteisAntes;
	}

	/**
	 * <p> Gera um QrCode com um texto e salva onde determina a variável @param filePath </p>
	 * @param text
	 * @param largura
	 * @param altura
	 * @param filePath
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void gerarQRCode(String text, int largura, int altura, String filePath)
			throws WriterException, IOException {
		File t = new File(filePath);
		if (!t.exists()) {
			FileUtils.forceMkdir(t);
		}

		HashMap config = new HashMap();
		config.put(EncodeHintType.MARGIN, -1);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, largura, altura, config);
		Path path = FileSystems.getDefault().getPath(filePath);
		MatrixToImageWriter.writeToPath(bitMatrix, path.toString().substring(path.toString().lastIndexOf(".")+1).toUpperCase(), path);
	}

	public static String completarEsquerda(String texto, int tamanho, String complemento) {
		String txt = texto;
		while (txt.length() < tamanho) {
			txt = complemento + txt;
		}
		return txt;
	}

	public static String getMesDaData(Calendar data) {
		if (data == null) return null;
		String mes = "";
		int m = data.get(Calendar.MONTH);
		switch (m) {
			case 0:mes = "Janeiro";break;
			case 1:mes = "Fevereiro";break;
			case 2:mes = "Março";break;
			case 3:mes = "Abril";break;
			case 4:mes = "Maio";break;
			case 5:mes = "Junho";break;
			case 6:mes = "Julho";break;
			case 7:mes = "Agosto";break;
			case 8:mes = "Setembro";break;
			case 9:mes = "Outubro";break;
			case 10:mes = "Novembro";break;
			case 11:mes = "Dezembro";break;
			default: mes = null;break;
		}

		return mes;
	}

	public static Time converterHoraStringParaTime(String hora) {
		if (hora == null || hora.trim().isEmpty()) return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			return new Time(sdf.parse(hora).getTime());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param pdfSemCpf
	 * @param marcaDagua
	 * @param distanciaX Afastamento entre duas marcas no eixo X
	 * @param distanciaY Afastamento entre duas marcas no eixo Y
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static File insereMarcaDaguaEmTodoArquivo(File pdfSemCpf, String marcaDagua, float distanciaX, float distanciaY)
			throws IOException, DocumentException {

		PdfReader reader = new PdfReader(pdfSemCpf.toURL());
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfSemCpf.getPath()));

		// text watermark
		Phrase frase = new Phrase(marcaDagua);
		frase.setFont(FontFactory.getFont("Helvetica"));

		// properties
		PdfContentByte over;
		Rectangle pagesize;

		// loop over every page
		int n = reader.getNumberOfPages();
		for (int i = 1; i <= n; i++) {

			// get page size and position
			pagesize = reader.getPageSizeWithRotation(i);

			over = stamper.getOverContent(i);
			over.saveState();

			// set transparency
			PdfGState state = new PdfGState();
			state.setFillOpacity(0.09f);
			over.setGState(state);

			for (float k = pagesize.getTop() - 10; k > pagesize.getBottom(); k -= distanciaY) {
				for (float j =  pagesize.getLeft() + 40; j < pagesize.getRight(); j += distanciaX) {
					ColumnText.showTextAligned(over, Element.ALIGN_CENTER, frase, j, k, 0);
				}
			}
			over.restoreState();

		}
		stamper.close();
		reader.close();

		return pdfSemCpf;
	}

	/**
	 * Essa função recebe números de 1 a 12 e retorna o nome do mês de Janeiro a Dezembro, onde Janeiro é igual a 1 e Dezembro é igual a 12.
	 * @param mes número de 1 a 12.
	 * @param abreviado boolean true ou false.
	 * @param maiusculo boolean true ou false.
	 * @return Nomes abreviados de Jan a Dez ou completo de Janeiro a Dezembro, podendo ainda ser em MAIÚSCULO.
	 */
	public static String getNomeMes(Integer mes, boolean abreviado, boolean maiusculo) {
		String nomeMes = "ERRO";
		switch (mes) {
			case 1:nomeMes = "Janeiro";break;
			case 2:nomeMes = "Fevereiro";break;
			case 3:nomeMes = "Março";break;
			case 4:nomeMes = "Abril";break;
			case 5:nomeMes = "Maio";break;
			case 6:nomeMes = "Junho";break;
			case 7:nomeMes = "Julho";break;
			case 8:nomeMes = "Agosto";break;
			case 9:nomeMes = "Setembro";break;
			case 10:nomeMes = "Outubro";break;
			case 11:nomeMes = "Novembro";break;
			case 12:nomeMes = "Dezembro";break;
		}

		return abreviado ? (maiusculo ? nomeMes.substring(0, 3).toUpperCase() : nomeMes.substring(0, 3)) : (maiusculo ? nomeMes.toUpperCase() : nomeMes);
	}

	public static Calendar getDia28MesAnteriorAoMesAnoInformado(Integer ano, Integer mes) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.MONTH, mes);
		data.set(Calendar.DAY_OF_MONTH, 1);

		return getDataHoraIncrementada(data, -2);
	}

	public static <T> T lerDadosRetornoConsultaIntegrada(HttpURLConnection conexao, Class<T> classOfT) throws IOException {
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			Gson gson = new GsonBuilder().create();
			Object o = gson.fromJson(response.toString(), classOfT);
			return classOfT.cast(o);
		}
	}
	
	public static boolean verificarDataValida(String data, String mascara) {
        try {
            //SimpleDateFormat é usada para trabalhar com formatação de datas
            //neste caso meu formatador irá trabalhar com o formato "dd/MM/yyyy"
            //dd = dia, MM = mes, yyyy = ano
            //o "M" dessa String é maiusculo porque "m" minusculo se n me engano é minutos
            SimpleDateFormat sdf = new SimpleDateFormat(mascara);
            //a mágica desse método acontece aqui, pois o setLenient() é usado para setar
            //sua escolha sobre datas estranhas, quando eu seto para "false" estou dizendo
            //que não aceito datas falsas como 31/02/2016
            sdf.setLenient(false);
            //aqui eu tento converter a String em um objeto do tipo date, se funcionar
            //sua data é valida
            sdf.parse(data);
            //se nada deu errado retorna true (verdadeiro)
            return true;
        } catch (ParseException ex) {
            //se algum passo dentro do "try" der errado quer dizer que sua data é falsa, então,
            //retorna falso
            return false;
        }
    }
	
	public static boolean dataMenorQueHoje(String data, String mascara) {
        //DateTimeFormatter tem uma função identica ao SimpleDateFormatter, mas escolhi usar ele
        //pois ele trabalha junto com o LocalDate que facilita muito trabalhar com datas
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mascara);
        //Aqui eu converto uma data em LocalDate e digo que quero no formato do DateTimeFormatter
        //que criei acima
        LocalDate dataVerificada = LocalDate.parse(data, dtf);
        //Esse comando pega a data de hoje
        LocalDate hoje = LocalDate.now();
        //Se dataVerificada comparada com hoje é menor ou igual a zero então retorna verdadeiro,
        //senão, retorna falso
       // com LocalDate não tem como comparar assim: data1 < data2
       //Tem que ser assim: data1.compareTo(data2)
       //quando a data1 é menor isso retorna -1, quando é maior retorna 1, e quando é igual retorna 0
       //por isso eu comparei com <=0 abaixo
        return dataVerificada.compareTo(hoje) < 0;
	}

	public static String getNomeCompletoDestacadoGuerra(String nomeCompleto, String nomeGuerra) {
		if (!nomeGuerra.contains(" ")) { 				// ----------------------------Caso o nome de guerra seja apenas um sobrenome
			nomeCompleto = nomeCompleto.replaceFirst(nomeGuerra, "<b>"+nomeGuerra+"</b>");
			return nomeCompleto;
		}
		String[] nomeGuerraDividido = nomeGuerra.split(" ");		// ----------------------------Caso o nome de guerra seja composto
		for (String nGuerra: nomeGuerraDividido) {
			if (nGuerra.length() == 1) {  				// ----------------------------Caso o nome de guerra contenha uma letra inicial de um sobrenome
				String[] temp = nomeCompleto.split(" ");
				for (String nomeDividido: temp) {
					if (nomeDividido.startsWith(nGuerra) && nomeDividido.length() > 3) {
						String nomeDivididoTemp = nomeDividido;
						nomeDividido = nomeDividido.replaceFirst(nGuerra, "<b>"+nGuerra+"</b>");
						nomeCompleto = nomeCompleto.replaceFirst(nomeDivididoTemp, nomeDividido);
					}
				}
			} else if (nGuerra.length() <= 3) { 		// ----------------------------Caso o nome de guerra contenha uma sílaba (do, dos, de, da...)
				nomeCompleto = nomeCompleto.replaceFirst(nGuerra+" ", "<b>"+nGuerra+"</b> ");
			} else {				// ----------------------------Caso o nome de guerra contenha 2 sobrenomes completos
				nomeCompleto = nomeCompleto.replaceFirst(nGuerra, "<b>"+nGuerra+"</b>");
			}
		}
		return nomeCompleto;
	}

	public static boolean validaRegexSenha(String senha) {
		return senha.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Za-z!@#$&*?\\d]{6,}$");
	}
}