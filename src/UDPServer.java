import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


    public class UDPServer {
        // [Constantes das cores]
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";

        public static void main(String[] args) {

            DatagramSocket asocket = null;
            Scanner ler = new Scanner(System.in);
            int suaPontuacao = 0, pontuacaoOponente = 0, roundCounter = 0;

            do {
                try {
                    // [Recebendo jogada do cliente]
                    asocket = new DatagramSocket(16868);
                    byte[] buffer = new byte[1000];
                    DatagramPacket request =
                            new DatagramPacket(buffer, buffer.length);
                    System.out.println("Aguardando jogada do cliente....");
                    asocket.receive(request);
                    String strJogadaCliente = new String(buffer, 0, request.getLength());

                    // [Menu]
                    int escolha;
                    Scanner in = new Scanner(System.in);
                    System.out.println(ANSI_BLUE + "======JOKENPO=======" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "O jogador que fizer 3 pontos primeiro ganha." + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "ROUND "+ (roundCounter+1) + "             " + ANSI_RESET);
                    System.out.print("Voce: ");
                    if(suaPontuacao != 0){
                        for (int j = 0; j < suaPontuacao; j++){
                            System.out.print(ANSI_GREEN + "*" + ANSI_RESET);
                        }
                    }
                    System.out.println();
                    System.out.print("Oponente: ");
                    if(pontuacaoOponente != 0){
                        for (int j = 0; j < pontuacaoOponente; j++){
                            System.out.print(ANSI_GREEN + "*" + ANSI_RESET);
                        }
                    }
                    System.out.println();
                    System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "|" + ANSI_RESET + " 1. Pedra         " + ANSI_BLUE + "|" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "|" + ANSI_RESET + " 2. Papel         " + ANSI_BLUE + "|" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "|" + ANSI_RESET + " 3. tesoura       " + ANSI_BLUE + "|" + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                    roundCounter++;
                    String jogada;
                    // [Fim do menu]

                    // [Captando jogada do servidor]
                    do {
                        System.out.println("Digite a jogada desejada: ");
                        System.out.println("Somente serão válidos valores entre 1 e 3.");
                        escolha = in.nextInt();
                    } while (escolha < 1 || escolha > 3);
                    switch (escolha) {
                        case 1:
                            System.out.println("Voce escolheu " + ANSI_BLUE + "PEDRA" + ANSI_RESET);
                            jogada = "Pedra";
                            break;
                        case 2:
                            System.out.println("Voce escolheu " + ANSI_BLUE + "PAPEL" + ANSI_RESET);
                            jogada = "Papel";
                            break;
                        case 3:
                            System.out.println("Voce escolheu " + ANSI_BLUE + "TESOURA" + ANSI_RESET);
                            jogada = "Tesoura";
                            break;
                        default:
                            jogada = "nada";
                            break;
                    }

                    // [Enviando jogada para o cliente]
                    byte[] m = jogada.getBytes();
                    DatagramPacket reply =
                            new DatagramPacket(m, m.length, request.getAddress(), request.getPort());
                    System.out.println("Jogada enviada...");
                    asocket.send(reply);

                    // [Exibindo resultado por partida]
                    if (jogada.equals(strJogadaCliente)) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_YELLOW + "  Houve um empate!");
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                    } else if (jogada.equals("Pedra") && strJogadaCliente.equals("Papel")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_RED + "  Voce perdeu!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        pontuacaoOponente++;
                    } else if (jogada.equals("Pedra") && strJogadaCliente.equals("Tesoura")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_GREEN + "  Voce ganhou!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        suaPontuacao++;
                    } else if (jogada.equals("Papel") && strJogadaCliente.equals("Pedra")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_GREEN + "  Voce ganhou!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        suaPontuacao++;
                    } else if (jogada.equals("Papel") && strJogadaCliente.equals("Tesoura")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_RED + "  Voce perdeu!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        pontuacaoOponente++;
                    } else if (jogada.equals("Tesoura") && strJogadaCliente.equals("Pedra")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_RED + "  Voce perdeu!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        pontuacaoOponente++;
                    } else if (jogada.equals("Tesoura") && strJogadaCliente.equals("Papel")) {
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        System.out.println("  Voce:     " + jogada);
                        System.out.println("  Oponente: " + strJogadaCliente);
                        System.out.println(ANSI_GREEN + "  Voce ganhou!" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "====================" + ANSI_RESET);
                        suaPontuacao++;
                    }

                    // [Exibindo resultado do jogo]
                    if(suaPontuacao == 3){
                        System.out.println(ANSI_GREEN + "  Parabens! Voce conseguiu os 3 pontos antes do seu oponente!" + ANSI_RESET);
                        break;
                    } else if(pontuacaoOponente == 3){
                        System.out.println(ANSI_GREEN + "  Ops... Parece que seu oponente conseguiu os 3 pontos antes de voce." + ANSI_RESET);
                        break;
                    }
                    
                } catch (SocketException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (asocket != null)
                        asocket.close();
                }
            }while(suaPontuacao != 3 || pontuacaoOponente !=3);
        }

    }

