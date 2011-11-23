import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import jp.co.csk.vdm.toolbox.VDM.CGException;

public class Main {

	private static Jogo jogo;
	private static Jogador j1;
	private static Jogador j2;
	private static BufferedReader reader;
	/**
	 * @param args
	 * @throws CGException 
	 */
	public static void main(String[] args) throws CGException {
		System.out.println("::Bem-vindo ao jogo Abalone::");
		initJogo();
		menu();
	}

	private static void initJogo() {
		try {
			j1 = new Jogador(1);
			j2 = new Jogador(2);
			jogo = new Jogo(j1,j2);
			casasInit();
		} catch (CGException e) {
			System.out.println("Erro ao criar o jogo.");
		}
	}

	@SuppressWarnings("unchecked")
	private static void casasInit() {
		try {
			jogo.tab.casas.clear();
			for(int i=0; i<61;i++)
			{
				Casa c = new Casa(i+1);
				jogo.tab.casas.add(c);
			}
			for(int i=0; i<11;i++)
				((Casa) jogo.tab.casas.get(i)).ColocaBola(j1.id);
			for(int i=13; i<16;i++)
				((Casa) jogo.tab.casas.get(i)).ColocaBola(j1.id);
			for(int i=45; i<48;i++)
				((Casa) jogo.tab.casas.get(i)).ColocaBola(j2.id);
			for(int i=50; i<61;i++)
				((Casa) jogo.tab.casas.get(i)).ColocaBola(j2.id);
		} catch (CGException e) {
			System.out.println("Erro na cria��o do tabuleiro em Java.");
		}
	}

	private static void menu() throws CGException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		int opt = 0;
		while(true){
			try {
				System.out.println("\n\n::: MENU :::");
				System.out.println("1) Novo Jogo");
				System.out.println("2) Continuar Jogo");
				System.out.println("3) Informa��es das Casas do Tabuleiro");
				System.out.println("4) Regras do Jogo");
				System.out.println("0) Sair");
				System.out.print("Escolha a op��o: ");
				opt = Integer.parseInt(reader.readLine());
				switch(opt){
				case 1:
					startGame(true);
					break;
				case 2:
					startGame(false);
					break;
				case 3:
					printInfo();
					break;
				case 4:
					printRegras();
					break;
				default:
					System.exit(0);
					break;
				}
			} catch (Exception e) {
				System.out.println("\nErro ao tentar ler o input do utilizador.\n");
			} 
		}
	}

	private static void startGame(boolean novo) throws CGException {
		boolean fimJogo = false;
		Jogada jogada;
		Vector<Movimento> movs;
		
		if(novo)
			initJogo();
		
		while(!fimJogo){
			movs = new Vector<Movimento>();
			int num_pecas;
			
			printTabOriginal();
			printTabJogo();
			printPontuacoes();
			printJogadorAtual();
			
			do{
				num_pecas = readInt("Quantas pe�as deseja mover (1-3) (x voltar ao menu)? ");
			}while(num_pecas >3 || num_pecas < 1);

			for(int i = 0; i<num_pecas;i++){
				int casa_origem;
				int casa_dest;
				
				do{
					casa_origem = readInt("Movimento " + (i+1) +": Escolha o n�mero da casa origem: ");
				}while(casa_origem < 1 || casa_origem > 61);
				
				do{
					casa_dest = readInt("Movimento " + (i+1) +": Escolha o n�mero da casa destino: ");
				}while(casa_dest < 1 || casa_dest > 61);
				
				Movimento m = new Movimento(jogo.tab,casa_origem,casa_dest);
				movs.add(m);
			}
			
			if(jogo.jogador_uatual)
				jogada = new Jogada(movs, jogo.tab.casas, jogo.jogador2.id);
			else
				jogada = new Jogada(movs, jogo.tab.casas, jogo.jogador1.id);

			if(jogada.RealizaJogada()){
				if(!jogo.AdicionaJogada(jogada))
					fimJogo=true;
				jogo.jogador_uatual = !jogo.jogador_uatual;
			}
			else
				System.out.println("JOGADA INV�LIDA!");
		}
		printTabOriginal();
		printTabJogo();
		printPontuacoes();
		printFim();

	}

	private static void printTabOriginal() {
		int linha_atual = 0;
		System.out.println("\n==== TABULEIRO ORIGINAL ====");
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			if(c.linha < 5){
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<6-c.linha;i++){
						System.out.print("  ");
					}
					linha_atual = c.linha;
				}
				if(c.num < 10){
					System.out.print(" 0"+c.num+" ");
				}
				else
					System.out.print(" "+c.num+" ");
			}
			else
			{
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<c.linha-4;i++){
						System.out.print("  ");
					}
					linha_atual = c.linha;
				}
				System.out.print(" "+c.num+" ");
			}
		}
		System.out.println();
	}

	private static void printTabJogo(){
		int linha_atual = 0;
		System.out.println("\n==== TABULEIRO DO JOGO =====");
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			if(c.linha < 5){
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<6-c.linha;i++){
						System.out.print("  ");
					}
					linha_atual = c.linha;
				}
				if(c.cor == j1.id)
					System.out.print(" o  ");
				else if(c.cor == j2.id)
					System.out.print(" x  ");
				else
					System.out.print(" -  ");
			}
			else
			{
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<c.linha-4;i++){
						System.out.print("  ");
					}
					linha_atual = c.linha;
				}
				if(c.cor == j1.id)
					System.out.print(" o  ");
				else if(c.cor == j2.id)
					System.out.print(" x  ");
				else
					System.out.print(" -  ");
			}
		}
		System.out.println("\n\n============================\n");
	}
	
	private static void printPontuacoes() {
		System.out.println("::Pontua��o::");
		System.out.println("Jogador1: "+(14-j1.num_ubolas));
		System.out.println("Jogador2: "+(14-j2.num_ubolas));
	}
	
	private static void printJogadorAtual() {
		if(jogo.jogador_uatual)
			System.out.println("\n� a vez do Jogador 1 (x)");
		else
			System.out.println("\n� a vez do Jogador 2 (o)");
	}
	
	private static void printFim() {
		if (jogo.jogador_uatual)
			System.out.println("PARAB�NS Jogador 2!!! GANHOU O JOGO!");
		else
			System.out.println("PARAB�NS Jogador 1!!! GANHOU O JOGO!");
	}

	private static void printInfo() {
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			System.out.println("Casa: "+c.num + "\n\tCor: "+c.cor+"\tLinha: "+c.linha);
		}
	}

	private static void printRegras() {
		System.out.println("\n--Objetivo do Jogo--");
		System.out.println("\tSer o primeiro a empurrar seis das bolas do advers�rio para fora do tabuleiro.");
		System.out.println("\n--O Jogo--");
		System.out.println("\tOs jogadores jogam � vez. As bolas pretas (x) come�am sempre o jogo.");
		System.out.println("\tS� podes fazer um \"Movimento\" na tua vez de jogar.");
		System.out.println("\tUm \"Movimento\" equivale � dist�ncia de uma s� casa.");
		System.out.println("\tPodes deslocar-te no tabuleiro em qualquer uma das seis dire��es.");
		System.out.println("\tS� podes fazer um \"Movimento\" se existir um espa�o livre adjacente.");
		System.out.println("\tUm \"Movimento\" pode incluir uma, duas ou tr�s bolas ao mesmo tempo. Se deslocares duas ou tr�s bolas na mesma jogada, � necess�rio que o fa�as na mesma dire��o.");
		System.out.println("\tN�o podes deslocar mais de tr�s bolas da mesma cor na mesma jogada.");
		System.out.println("\tPodes desfazer uma fila mais longa j� existente deslocando uma, duas ou tr�s bolas da mesma cor.");
		System.out.println("\n--Movimentos--");
		System.out.println("\tUm movimento \"em linha\": as bolas deslocam-se a direito para a casa seguinte.");
		System.out.println("\tUma desloca��o \"em flecha\": as bolas deslocam-se lateralmente para a casa seguinte.");
		System.out.println("\tDepois de realizado um Movimento n�o pode ser mudado.");
		System.out.println("\n--Sumito--");
		System.out.println("\tPodes empurrar as bolas do teu advers�rio se conseguires criar um \"Sumito\", uma situa��o em que as tuas bolas s�o mais numerosas do que as do teu advers�rio.\n\tExistem apenas tr�s tipo de Sumito:");
		System.out.println("\t\tSumito de 2 para 1;");
		System.out.println("\t\tSumito de 3 para 1;");
		System.out.println("\t\tSumito de 3 para 2.");
		System.out.println("\tDepois de teres criado um Sumito, s� podes empurrar as bolas do teu advers�rio:");
		System.out.println("\t\tusando um movimento \"em linha\";");
		System.out.println("\t\tquando as bolas pretas (x) e brancas (o) estiverem em espa�os diretamente adjacentes entre si;");
		System.out.println("\t\tquando existe uma casa livre atr�s da(s) bola(s) da defesa.");
		System.out.println("\tMesmo que seja poss�vel um movimento de Sumito, � sempre opcional. N�o �s obrigado a empurrar o teu advers�rio se n�o o quiseres fazer.");
		System.out.println("\n--Pac--");
		System.out.println("\tNesta situa��o, as pe�as de ambos os jogadores est�o em posi��o de igualdade, tornando imposs�vel um jogador obter vantagem sobre o outro.");
		System.out.println("\tExistem apenas tr�s tipo de Pac:");
		System.out.println("\t\tPac de 1 para 1;");
		System.out.println("\t\tPac de 2 para 2;");
		System.out.println("\t\tPac de 3 para 3.");
		System.out.println("\tQuando um jogador tem mais de tr�s bolas imobilizadas num Pac, estas bolas extra n�o s�o tomadas em considera��o.\n\tUm Pac de 4 para 3 na realidade � o mesmo do que um Pac de 3 para 3.");
		System.out.println("\tPara quebrar o impasse, um dos jogadores ter� de acabar com o Pac aproximando uma linha de ataque diferente.");
		System.out.println("\n--Elimina��o--");
		System.out.println("\tUma bola � eliminada quando � empurrada para fora do tabuleiro.");
		System.out.println("\n--Ganhar o Jogo--");
		System.out.println("\tSe fores o primeiro jogador a empurrar seis bolas do teu advers�rio para fora do tabuleiro ganhas o jogo!");
	}

	private static int readInt(String msg){
		boolean erro = true;
		int opt = 0;
		while(erro){
			try {
				System.out.print(msg);
				String r =reader.readLine();
				if(r.equals("x"))
					menu();
				else
					opt = Integer.parseInt(r);
				erro = false;
			} catch (Exception e) {
				System.out.println();
				System.out.println("Erro ao tentar ler o input do utilizador.");
			} 
			System.out.println();
		}
		return opt;
	}
}
