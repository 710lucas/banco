import java.util.Iterator;
import java.util.Scanner;

import banco.entidades.*;
import banco.entidades.util.*;


public class Banco {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
    

        int acao = 0;
        boolean sair = false, sairLogin = false;
        final int CRIAR = 1, USAR_EXISTENTE = 2, SAIR_LOGIN = 3;
        final int DEPOSITAR = 1, SACAR = 2, EXTRATO = 3, TRANSFERIR = 4, SALDO = 5, SAIR = 6;

        Agencia.abrirCaixa();

        System.out.println("Contas existentes");
        Iterator it = Agencia.getContas();
        while(it.hasNext())
            System.out.println(it.next());

        ContaSimples conta = null;

        do{
            acao = recebeInputInt("1 - Criar nova conta\n2 - Usar conta existente\n3 - Sair\n>");
            switch(acao){
                case CRIAR:
                    conta = criarConta();
                    Agencia.addConta(conta);

                    if(conta == null) {
                        System.err.println("Houve um erro ao criar a conta");
                        break;
                    }

                    System.out.println("O numero da sua conta é: " + conta.getNúmero());
                    sairLogin = true;
                    break;

                case USAR_EXISTENTE:
                    conta = pegaContaExistente();

                    if(conta == null){
                        System.err.println("Numero de conta invalido, tente novamente");
                        break;
                    }

                    Agencia.addConta(conta);

                    System.out.println("Te damos as boas vindas, " + conta.getNome());
                    sairLogin = true;
                    break;

                case SAIR_LOGIN:
                    sairLogin  = true;
                    break;

                default:
                    break;
            }
        }while(!sairLogin); //3 -sair


        do{
            acao = recebeInputInt("1 - Depositar\n2 - Sacar\n3 - Extrato\n4 - Transferir\n5 - Saldo\n6 - Sair\n>");

            switch(acao){
                case DEPOSITAR:
                    depositar(conta);
                    break;
                case SACAR:
                    sacar(conta);
                    break;
                case EXTRATO:
                    extrato(conta);
                    break;
                case TRANSFERIR:
                    transferir(conta);
                    break;
                case SALDO:
                    System.out.println("Seu saldo é: "+ conta.getSaldo());
                    break;
                case SAIR:
                    System.out.println("Seu saldo é: "+ conta.getSaldo());
                    sair = true;
                    break;
                default:
                    break;
            }

        }while(!sair);

        Agencia.fecharCaixa();

    }

    public static int recebeInputInt(String msgParaPrintar){
        System.out.print(msgParaPrintar);
        int returnValue = sc.nextInt();
        sc.nextLine();
        return returnValue;
    }

    public static String recebeInputString(String msgParaPrintar){
        System.out.print(msgParaPrintar);
        return sc.nextLine();
    }

    public static ContaSimples criarConta(){
        String nome = recebeInputString("Informe seu nome: ");
        String cpf = recebeInputString("Informe seu cpf: ");

        return new ContaSimples(nome, cpf);

    }

    public static ContaSimples pegaContaExistente() {
        return (ContaSimples) Agencia.localizarConta(recebeInputInt("Digite o numero da sua conta: "));
    }


    public static void depositar(Conta conta){
        System.out.print("Digite a quantidade que deseja depositar: ");
        double quantidade = sc.nextDouble();
        sc.nextLine();
        conta.depositar(quantidade);
    }

    public static void sacar(Conta conta){
        System.out.print("Digite a quantidade que deseja sacar: ");
        double quantidade = sc.nextDouble();
        sc.nextLine();
        conta.sacar(quantidade);
    }

    public static void extrato(Conta conta){

        System.out.println("Digite a data inicial do extrato no formato dia/mes/ano: ");
        String dataInicialString = sc.nextLine();
        int[] dataInicial = separaData(dataInicialString);

        System.out.println("Digite a data final do extrato no formato dia/mes/ano: ");
        String dataFinalString = sc.nextLine();
        int[] dataFinal = separaData(dataFinalString);

        System.out.println(conta.criarExtrato(new Data(dataInicial[0], dataInicial[1], dataInicial[2]), new Data(dataFinal[0], dataFinal[1], dataFinal[2])).formatar());
    }

    public static void transferir(Conta conta){
        Iterator contas = Agencia.getContas();
        System.out.println("Contas disponiveis para fazer a transferência: ");
        int i = 0;
        while(contas.hasNext()){
            System.out.println("[Numero da conta: "+i+"] "+contas.next());
            i++;
        }
        System.out.print("Escreva o numero da pessoa para a qual você deseja fazer a transferência: ");
        int numero = sc.nextInt();
        sc.nextLine();

        System.out.print("Digite a quantidade desejada para transferir: ");
        double valor = sc.nextDouble();
        sc.nextLine();


        if(Agencia.localizarConta(numero) == null){
            System.err.println("Numero invalido, tente novamente");
            transferir(conta);
            return;
        }
        conta.transferir(Agencia.localizarConta(numero), valor);
    }

    public static int[] separaData(String data){

        int[] diasMesesAnos = new int[3];
        int contador = 0;

        String tmpString="";
        for(int i = 0; i<data.length(); i++){
            if(data.charAt(i) != '/'){
                tmpString+=data.charAt(i);
            }
            if(data.charAt(i) == '/' || i == data.length()-1){

                diasMesesAnos[contador]=Integer.parseInt(tmpString);
                System.out.println(diasMesesAnos[contador]);
                tmpString = "";
                contador++;
            }
        }

        diasMesesAnos[1]-=1;

        return diasMesesAnos;

    }

    
}
