package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa uma despesa
 * 
 * @author Rodrigo Barrocas n53680
 */
public class Expense {

    private String descricao;
    private User userPagante;
    private Integer valor;
    private List<String> envolvidos;
    private Map<String, Double> comoDividir;

    /**
     * Construtor para quando o split é igual para todos os envolvidos
     * 
     * @param description
     * @param userWhoPaid
     * @param value
     * @param envolvidos
     * 
     */
    public Expense(String description, User userWhoPaid, Integer value, List<String> envolvidos) {
        // Chama o outro construtor com o howToSplit = null
        this(description, userWhoPaid, value, envolvidos, null);
    }

    /**
     * Construtor para quando o split é diferente para cada um dos envolvidos
     * 
     * @param description
     * @param userWhoPaid
     * @param value
     * @param envolvidos
     * 
     * @param howToSplit
     */
    public Expense(String description, User userWhoPaid, Integer value, List<String> envolvidos,
            List<Double> howToSplit) {
        this.descricao = description;
        this.userPagante = userWhoPaid;
        this.valor = value;
        this.envolvidos = envolvidos;
        // Se o howToSplit for null, então o split é igual para todos os envolvidos
        if (howToSplit != null) {
            // Cria um HashMap com o username e o split correspondente
            this.comoDividir = new HashMap<>();
            // Percorre a lista de howToSplit e adiciona ao HashMap
            for (int i = 0; i < envolvidos
                    .size(); i++) {
                this.comoDividir.put(envolvidos
                        .get(i), howToSplit.get(i));
            }
        }
    }

    /**
     * Getters
     * 
     * @return value
     */
    public Integer getValor() {
        return valor;
    }

    /**
     * Getters
     * 
     * @return envolvidos
     * 
     */
    public User getPayer() {
        return userPagante;
    }

    /**
     * Getters
     * 
     * @return description
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Calcula o split para um dado username
     * 
     * @param username
     * @return split
     */
    public Integer getSplit(String username) {
        // Se o howToSplit for null, então o split é igual para todos os envolvidos
        if (comoDividir == null) {
            return valor / envolvidos.size();
        } else {
            // Se o howToSplit não for null, então o split é diferente para cada um dos
            // envolvidos
            return (int) (valor * comoDividir.getOrDefault(username, 0.0));
        }
    }

    /**
     * Calcula o balanço para um dado username
     * 
     * @param username
     * @return
     */
    public Integer getExpenseBalance(String username) {
        // Se o username for o userWhoPaid, então o balanço é o valor da despesa menos o
        // split
        if (username.equals(userPagante.getUsername())) {
            return valor - getSplit(username);
        } else {
            // Se o username não for o userWhoPaid, então o balanço é o split menos o valor
            // da despesa
            return -getSplit(username);
        }
    }

}
