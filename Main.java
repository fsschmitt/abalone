import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
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
		
		initJogo();
        System.out.println("::Bem vindo ao jogo Abalone!::\n\n");
        menu();
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
	
	private static void menu() throws CGException {
		boolean erro = true;
        reader = new BufferedReader(new InputStreamReader(System.in));
        int opt = 0;
		while(erro){
	        try {
	        	System.out.println("::: MENU :::");
	        	System.out.println("1 - Novo Jogo");
	        	System.out.println("2 - Informações das Casas do Tabuleiro");
	            System.out.println("0 - Sair");
	        	System.out.print("Insira a opcao: ");
				opt = Integer.parseInt(reader.readLine());
				erro = false;
			} catch (Exception e) {
				System.out.println();
				System.out.println("Erro ao tentar ler o input do utilizador.");
			} 
        }
			switch(opt){
	    	case 1:
	    		startGame();
	    		break;
	    	case 2:
	    		printInfo();
	    		break;
	    	default:
	    		System.exit(0);
	    		break;
			}
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
			System.out.println("Erro na criação do tabuleiro em Java.");
		}
	}

	private static void startGame() throws CGException {
		boolean fimJogo = false;
		Jogada jogada;
		Vector<Movimento> movs;
		while(!fimJogo){
			printTabOriginal();
			printTabJogo();
			System.out.println("::Pontuação::");
			System.out.println("Jogador1: "+(14-j1.num_ubolas));
			System.out.println("Jogador2: "+(14-j2.num_ubolas));
			movs = new Vector<Movimento>();
			if(jogo.jogador_uatual)
				System.out.println("É a vez do Jogador 1 (o)");
			else
				System.out.println("É a vez do Jogador 2 (x)");
			int num_pecas;
			do{
				num_pecas = readInt("Quantas peças deseja mover? (1-3)");
			}while(num_pecas >3 || num_pecas < 1);
			
			
			for(int i = 0; i<num_pecas;i++){
				int casa_origem;
				int casa_dest;
				do{
					casa_origem = readInt("Escolha o número da casa origem: ");
					System.out.println();
				}while(casa_origem < 1 || casa_origem > 61);
				do{
					casa_dest = readInt("Escolha o número da casa destino: ");
					System.out.println();
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
			}
			else{
				System.out.println("JOGADA INVALIDA!");
				jogo.jogador_uatual = !jogo.jogador_uatual;
			}
			
			jogo.jogador_uatual = !jogo.jogador_uatual;
			
		}
		printTabOriginal();
		printTabJogo();
		System.out.println("::Pontuação::");
		System.out.println("Jogador1: "+(14-j1.num_ubolas));
		System.out.println("Jogador2: "+(14-j2.num_ubolas));
		System.out.println("PARABÉNS ACABOU DE GANHAR O JOGO!");
		
	}

	private static void printInfo() {
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			System.out.println("Casa numero: "+c.num + "\nCor: "+c.cor+"\nLinha: "+c.linha);
		}
	}

	private static void printTabOriginal() {
		int linha_atual = 0;
		System.out.println("==== TABULEIRO ORIGINAL ====");
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			if(c.linha < 5){
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<6-c.linha;i++){
						System.out.print(" ");
					}
					linha_atual = c.linha;
				}
				if(c.num < 10){
					System.out.print(" "+c.num+" ");
				}
				else
					System.out.print(c.num+" ");
			}
			else
			{
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<c.linha-4;i++){
						System.out.print(" ");
					}
					linha_atual = c.linha;
				}
					if(c.num == 57 || c.num == 51)
						System.out.print(" ");
					System.out.print(c.num+" ");
			}
		}
		System.out.println();
		System.out.println("============================");
	}
	
	private static void printTabJogo(){
		int linha_atual = 0;
		System.out.println("==== TABULEIRO DO JOGO =====");
		for(Object o : jogo.tab.casas){
			Casa c = (Casa) o;
			if(c.linha < 5){
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<6-c.linha;i++){
						System.out.print(" ");
					}
					linha_atual = c.linha;
				}
				if(c.cor == j1.id)
					System.out.print("x ");
				else if(c.cor == j2.id)
					System.out.print("o ");
				else
					System.out.print("- ");
			}
			else
			{
				if(c.linha != linha_atual){
					System.out.println();
					for(int i = 0; i<c.linha-4;i++){
						System.out.print(" ");
					}
					linha_atual = c.linha;
				}
				if(c.cor == j1.id)
					System.out.print("x ");
				else if(c.cor == j2.id)
					System.out.print("o ");
				else
					System.out.print("- ");
			}
		
		}
		System.out.println();
		System.out.println("============================");

	}

}
