import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Veiculo {
    private String modeloVeiculo;
    private String placaVeiculo;

    public Veiculo(String modeloVeiculo, String placaVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
        this.placaVeiculo = placaVeiculo;
    }

    public String getModelo() {
        return modeloVeiculo;
    }

    public String getPlaca() {
        return placaVeiculo;
    }

    public void detalhes() {
        System.out.println("Modelo: " + modeloVeiculo);
        System.out.println("Placa: " + placaVeiculo);
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }
}

class Ticket {
    private String hrrEntrada;
    private Date diaEntrada;
    private Vaga vaga;
    private Veiculo veiculo;

    public Ticket(String hrrEntrada, Date diaEntrada, Vaga vaga, Veiculo veiculo) {
        this.hrrEntrada = hrrEntrada;
        this.diaEntrada = diaEntrada;
        this.vaga = vaga;
        this.veiculo = veiculo;
    }

    public String getHorarioEntrada() {
        return hrrEntrada;
    }

    public Date getDiaEntrada() {
        return diaEntrada;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Ticket [Horário: " + hrrEntrada +
               ", Dia: " + sdf.format(diaEntrada) +
               ", Vaga: " + vaga.getNumero() +
               ", Veículo: " + veiculo.getModelo() +
               " (" + veiculo.getPlaca() + ")]";
    }
}

class Vaga {
    private int numero;
    private boolean disponivel;

    public Vaga(int numero) {
        this.numero = numero;
        this.disponivel = true;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void ocupar() {
        this.disponivel = false;
    }

    public void liberar() {
        this.disponivel = true;
    }
}

class Estacionamento {
    private List<Vaga> vagas;
    private List<Ticket> tickets;

    public Estacionamento(int quantidadeVagas) {
        vagas = new ArrayList<>();
        tickets = new ArrayList<>();
        for (int i = 1; i <= quantidadeVagas; i++) {
            vagas.add(new Vaga(i));
        }
    }

    public Ticket gerarTicket(String hrrEntrada, String diaEntradaStr, Veiculo veiculo) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date diaEntrada = sdf.parse(diaEntradaStr);

            for (Vaga vaga : vagas) {
                if (vaga.isDisponivel()) {
                    vaga.ocupar();
                    Ticket ticket = new Ticket(hrrEntrada, diaEntrada, vaga, veiculo);
                    tickets.add(ticket);
                    return ticket;
                }
            }
            System.out.println("Não há vagas disponíveis!");
        } catch (ParseException | IllegalArgumentException e) {
            System.out.println("Data inválida! use o formato dd/MM/yyyy");
        }

        return null;
    }

    public void liberarVaga(int numeroVaga) {
        for (Vaga vaga : vagas) {
            if (vaga.getNumero() == numeroVaga && !vaga.isDisponivel()) {
                vaga.liberar();
                System.out.println("Vaga " + numeroVaga + " liberada!");
                return;
            }
        }
        System.out.println("Vaga inválida ou já disponível.");
    }

    public void listarTickets() {
        if (tickets.isEmpty()) {
            System.out.println("Nenhum ticket gerado.");
        } else {
            for (Ticket ticket : tickets) {
                System.out.println(ticket);
            }
        }
    }

    public void listarVagas() {
        for (Vaga vaga : vagas) {
            String status = vaga.isDisponivel() ? "Disponível" : "Ocupada";
            System.out.println("Vaga " + vaga.getNumero() + ": " + status);
        }
    }
}

public class Shopping {
    private final Estacionamento estacionamento;

    public Shopping(int quantidadeVagas) {
        estacionamento = new Estacionamento(quantidadeVagas);
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n==== Menu Estacionamento ====");
            System.out.println("1. Gerar ticket");
            System.out.println("2. Liberar vaga");
            System.out.println("3. Listar vagas");
            System.out.println("4. Listar tickets");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = 0;

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Digite o modelo do veículo: ");
                    String modelo = scanner.nextLine();
                    System.out.print("Digite a placa do veículo: ");
                    String placa = scanner.nextLine();
                    System.out.print("Digite o horário de entrada (ex: 14:30): ");
                    String horario = scanner.nextLine();
                    System.out.print("Digite o dia de entrada (ex: 10/04/2025): ");
                    String dia = scanner.nextLine();

                    Veiculo veiculo = new Veiculo(modelo, placa);
                    Ticket ticket = estacionamento.gerarTicket(horario, dia, veiculo);
                    if (ticket != null) {
                        System.out.println("Ticket gerado: " + ticket);
                    }
                    break;
                case 2:
                    System.out.print("Digite o número da vaga para liberar: ");
                    int vagaLiberar = Integer.parseInt(scanner.nextLine());
                    estacionamento.liberarVaga(vagaLiberar);
                    break;
                case 3:
                    estacionamento.listarVagas();
                    break;
                case 4:
                    estacionamento.listarTickets();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida! Por favor, escolha uma opção válida.");
                    break;
            }
        }
    }
}