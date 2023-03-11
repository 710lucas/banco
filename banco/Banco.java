package banco;
import java.util.Iterator;
import java.util.Scanner;

import banco.entidades.Agencia;
import banco.entidades.Conta;
import banco.entidades.ContaSimples;
import banco.entidades.Pessoa;
import banco.entidades.util.Data;
import banco.entidades.GeradorDeNumeroDeConta;

public class Banco {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
    


        int acao = 0;
        boolean sair = false;

        Agencia agencia = new Agencia();
        agencia.abrirCaixa();
        Iterator it = agencia.getContas();
        while(it.hasNext()){
            System.out.println(it.next());
        }

        Conta conta;

        while(acao != 1 && acao !=2 && acao != 3 && !sair){
            System.out.print("1- Criar nova conta\n2- Usar conta existente\n3- Sair\n>");
            acao = sc.nextInt();
            sc.nextLine();

            if(acao == 3) {
                sair = true;
                break;
            }

            if(acao == 1){
                getQuantidadeDeContas(agencia);
                System.out.print("Informe seu nome: ");
                String nome = sc.nextLine();
                System.out.print("Informe seu cpf: ");
                String cpf = sc.nextLine();

//                int quantidade =

                Conta contaNova = new ContaSimples(new Pessoa(nome, cpf));
                agencia.addConta(contaNova);

                System.out.println("O numero da sua conta é: "+contaNova.getNúmero()+"\nMemorize esse numero");

                conta = contaNova;
            }

            else{
                System.out.print("Informe o numero da sua conta: ");
                int numero = sc.nextInt();
                sc.nextLine();

                conta = Agencia.localizarConta(numero);

                if(conta == null){
                    System.err.println("Numero não existe");
                    System.exit(0);
                }

                System.out.println(conta.getNome());

            }


            while(!sair){
                System.out.print("1- Depositar\n2- Sacar\n3- Extrato\n4- Transferir\n5- Saldo\n6- Sair\n>");
                acao = sc.nextInt();
                sc.nextLine();
                switch(acao){

                    case 1:
                        depositar(conta);
                        break;

                    case 2:
                        sacar(conta);
                        break;

                    case 3:
                        extrato(conta);
                        break;

                    case 4:
                        transferir(conta, agencia);
                        break;

                    case 5:
                        System.out.println("Seu saldo é: "+conta.getSaldo());
                        break;

                    case 6:
                        System.out.println("Seu saldo é: "+conta.getSaldo());
                        sair = true;
                        break;

                    default:
                        break;
                }
            }


        }

        Agencia.fecharCaixa();
        

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

    public static void transferir(Conta conta, Agencia agencia){
        Iterator contas = agencia.getContas();
        System.out.println("Contas disponiveis para fazer a transferência: ");
        int i = 1;
        while(contas.hasNext()){
            System.out.println("NUMERO: "+i+" "+contas.next());
            i++;
        }
        System.out.print("Escreva o numero da pessoa para a qual você deseja fazer a transferência: ");
        int numero = sc.nextInt();
        sc.nextLine();
        System.out.print("Digite a quantidade desejada para transferir: ");
        double valor = sc.nextDouble();
        sc.nextLine();
        conta.transferir(agencia.localizarConta(numero), valor);
    }

    public static int getQuantidadeDeContas(Agencia agencia){
        int quantidade = 0;
        Iterator contasIt = agencia.getContas();
        while(contasIt.hasNext()){
            quantidade++;
            contasIt.next();

        }
        for(int i = 1; i<quantidade; i++){
            GeradorDeNumeroDeConta.geraNumero();
        }


        return quantidade;
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
