package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa um grupo de despesas
 * @author Rodrigo Barrocas n53680
 */

public class ExpensesGroup {

	private String groupName;
	private User creator;
	private List<User> usersInGroup;
	private List<Expense> expenses;
	private Map<String, Map<String, Integer>> debtGraph; // QuemÉdevido -> QuemDeve, QtDeve

	public ExpensesGroup(String groupName, User user) {
		this.groupName = groupName;
		this.creator = user;
		this.usersInGroup = new ArrayList<>();
		this.usersInGroup.add(creator);
		this.expenses = new ArrayList<>();
		this.debtGraph = new HashMap<>();
		//Adiciona o grupo ao utilizador que o criou
		user.addGroup(this);
	}

	
	/** 
	 * Retorna o criador do grupo
	 * @return User
	 */
	public User getCreator() {
		return creator;
	}

	
	/** 
	 * Retorna o nome do grupo
	 * @return String
	 */
	public String getGroupName() {
		return groupName;
	}

	
	/** 
	 * Retorna uma lista com os usernames dos utilizadores no grupo
	 * @return List<String>
	 */
	public List<String> usersInGroup() {
		// Cria uma lista de strings para guardar os usernames
		List<String> usernames = new ArrayList<>();
		for (User user : usersInGroup) {
			// Adiciona o username à lista
			usernames.add(user.getUsername());
		}
		// Retorna a lista de usernames
		return usernames;
	}

	
	/** 
	 * Retorna uma lista com os usernames dos utilizadores no grupo
	 * @return List<Expense>
	 */
	public List<Expense> getExpenses() {
		return expenses;
	}

	
	/** 
	 * Adiciona um utilizador ao grupo
	 * @param user
	 */
	public void addUser(User user) {
		// Adiciona o utilizador ao grupo
		usersInGroup.add(user);
		// Adiciona o grupo ao utilizador
		user.addGroup(this);
	}

	
	/** 
	 * Adiciona uma despesa ao grupo
	 * 
	 * @param description
	 * @param userWhoPaid
	 * @param value
	 */
	public void addExpense(String description, User userWhoPaid, Integer value) {
		// Cria uma nova despesa
		Expense expense = new Expense(description, userWhoPaid, value, usersInGroup());
		// Adiciona a despesa à lista de despesas
		expenses.add(expense);
		// Atualiza o mapa de dividas
		updateDebtGraph(expense, null);

	}

	
	/** 
	 * Adiciona uma despesa ao grupo
	 * 
	 * Metodo suporta forma de dividir a despesa diferente para cada um dos
	 * membros do grupo
	 * @param description
	 * @param userWhoPaid
	 * @param value
	 * @param howToSplit
	 */
	public void addExpense(String description, User userWhoPaid, Integer value, List<Double> howToSplit) {
		// Cria uma nova despesa
		Expense expense = new Expense(description, userWhoPaid, value, usersInGroup(), howToSplit);
		// Adiciona a despesa à lista de despesas
		expenses.add(expense);
		// Atualiza o mapa de dividas
		updateDebtGraph(expense, howToSplit);

	}

	
	/** 
	 * Atualiza o mapa de dividas do grupo
	 * @param expense
	 * @param howToSplit
	 */
	private void updateDebtGraph(Expense expense, List<Double> howToSplit) {
		// Obtem o utilizador que pagou a despesa
		User utilizadorPagante = expense.getPayer();
		// Obtem a lista de utilizadores envolvidos na despesa
		List<String> utilizadoresEnvolvidos = usersInGroup();
		// Cria um iterador para percorrer a lista de utilizadores envolvidos
		Iterator<String> iterador = utilizadoresEnvolvidos.iterator();

		// Numero de utilizadores que devem ter acrescimo de 1 (caso divisão não seja
		// inteira)
		int numUtilizadoresAcrescimo = expense.getValor() % utilizadoresEnvolvidos.size(),
				contador = 0;

		// Percorre a lista de utilizadores envolvidos
		while (iterador.hasNext()) {
			// Obtem o utilizador que deve
			String utilizadorDevedor = iterador.next();
			// Se o utilizador que deve não for o utilizador que pagou
			if (!utilizadorDevedor.equals(utilizadorPagante.getUsername())) {
				// Se não estiver definida a forma de divisão
				if (howToSplit == null) {
					// Adiciona a divida ao utilizador que deve
					addDebt(utilizadorPagante.getUsername(), utilizadorDevedor,
							expense.getValor() / utilizadoresEnvolvidos.size(), contador < numUtilizadoresAcrescimo);
				} else {
					// Adiciona a divida ao utilizador que deve
					addDebt(utilizadorPagante.getUsername(), utilizadorDevedor,
							(int) (expense.getValor()
									* howToSplit.get(utilizadoresEnvolvidos.indexOf(utilizadorDevedor))),
							contador < numUtilizadoresAcrescimo);
				}
			}
			contador++;
		}

	}

	
	/** 
	 * Adiciona uma divida ao mapa de dividas
	 * Atualiza todo o mapa de dividas tanto para o utilizador que pagou como para o
	 * utilizador que deve
	 * @param utilizadorPagante
	 * @param utilizadorDevedor
	 * @param valorDivida
	 * @param temAcrescimo
	 */
	private void addDebt(String utilizadorPagante, String utilizadorDevedor, int valorDivida,
			boolean temAcrescimo) {
		// Se o usuário não tem dividas, cria uma nova entrada no mapa
		debtGraph.putIfAbsent(utilizadorPagante, new HashMap<>());
		// Obter as dividas de ambos os utilizadores
		Integer dividaAtualDevedor = debtGraph.get(utilizadorPagante).get(utilizadorDevedor) == null ? valorDivida
				: debtGraph.get(utilizadorPagante).get(utilizadorDevedor) + valorDivida;
		Integer dividaAtualPagante = debtGraph.containsKey(utilizadorDevedor)
				? (debtGraph.get(utilizadorDevedor).get(utilizadorPagante) == null ? 0
						: debtGraph.get(utilizadorDevedor).get(utilizadorPagante))
				: 0;
		// Se tiver acrescimo incrementa o valor 1 ao utilizadorDevedor
		dividaAtualDevedor += temAcrescimo ? 1 : 0;

		// Se o utilizadorPaganete dever mais que o utilizadorDevedor do que o
		// utilizadorDevedor deve ao utilizadorPagante
		if (dividaAtualPagante > dividaAtualDevedor) {
			// Subtrai o valor da divida atual do utilizadorPagante com o valor da divida
			// atual do utilizadorDevedor
			dividaAtualPagante -= dividaAtualDevedor;
			// Atualiza o valor da divida no mapa
			debtGraph.get(utilizadorDevedor).put(utilizadorPagante, dividaAtualPagante);
			// Se o utilizadorPagante não deve nada ao utilizadorDevedor
			if (debtGraph.containsKey(utilizadorPagante)) {
				debtGraph.get(utilizadorPagante).remove(utilizadorDevedor);
			}
			// Se o utilizadorDevedor dever mais que o utilizadorPagante do que o
			// utilizadorPagante deve ao utilizadorDevedor
		} else if (dividaAtualDevedor > dividaAtualPagante) {
			// Subtrai o valor da divida atual do utilizadorDevedor com o valor da divida
			// atual do utilizadorPagante
			dividaAtualDevedor -= dividaAtualPagante;
			// Atualiza o valor da divida no mapa
			debtGraph.get(utilizadorPagante).put(utilizadorDevedor, dividaAtualDevedor);
			// Se o utilizadorPagante deve ao utilizadorDevedor
			if (debtGraph.containsKey(utilizadorDevedor)) {
				// Apaga a divida do utilizadorPagante para com o utilizadorDevedor
				debtGraph.get(utilizadorDevedor).remove(utilizadorPagante);
			}
		} else {
			// Se o utilizadorPagante e o utilizadorDevedor tiverem a mesma divida
			// Remove a divida do utilizadorPagante para com o utilizadorDevedor
			debtGraph.get(utilizadorPagante).remove(utilizadorDevedor);
			// Remove a divida do utilizadorDevedor para com o utilizadorPagante
			debtGraph.get(utilizadorDevedor).remove(utilizadorPagante);
		}

	}

	
	/** 
	 * Retorna o saldo do usuário
	 * @param user
	 * @return Integer
	 */
	public Integer getBalance(User user) {
		// Inicializa o saldo do usuário
		Integer balance = 0;
		// Obtem o nome do usuário
		String username = user.getUsername();
		// Se o usuário for pagante, ele deve receber o valor devido pelos outros
		if (debtGraph.containsKey(username)) {
			// Mapa de quem deve ao usuário
			Map<String, Integer> debtorMap = debtGraph.get(username);
			// Percorre o mapa de quem deve ao usuário
			for (Integer value : debtorMap.values()) {
				// Soma o valor devido ao usuário
				balance += value;
			}
		}
		// Temos que verificar todas as pessoas que o usuário deve
		// Percorre o mapa de quem o usuário deve
		for (Map<String, Integer> map : debtGraph.values()) {
			// Se o usuário deve a alguém
			if (map.containsKey(username)) {
				// Subtrai o valor devido pelo usuário
				balance -= map.get(username);
			}
		}
		return balance;
	}

	
	/** 
	 * Retorna um mapa com as dividas para o usuário
	 * @param user
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getUserDebtors(User user) {
		// Se o usuário não tiver dividas, retorna um mapa vazio
		return debtGraph.get(user.getUsername()) == null ? new HashMap<>() : debtGraph.get(user.getUsername());

	}

	
	/** 
	 * Retorna um mapa com as dividas do usuário
	 * @param user
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getUserDebts(User user) {
		Map<String, Integer> dividas = new HashMap<>();
		// Temos que verificar todas as pessoas que o usuário deve
		// Percorre o mapa de quem o usuário deve
		debtGraph.values().forEach(map -> {
			if (map.containsKey(user.getUsername())) {
				if (map.get(user.getUsername()) > 0) {
					// adiciona a divida ao mapa
					dividas.put(getKeyPeloValor(debtGraph, map), map.get(user.getUsername()));
				}

			}
		});
		return dividas;

	}

	/**
	 * Método que retorna a chave de um mapa a partir do valor
	 * 
	 * @param debtGraph2
	 * @param map
	 * @return
	 */
	private String getKeyPeloValor(Map<String, Map<String, Integer>> debtGraph2, Map<String, Integer> map) {
		// Percorre o mapa
		for (String key : debtGraph2.keySet()) {
			// Se o valor do mapa for igual ao valor do mapa passado como parâmetro
			if (debtGraph2.get(key).equals(map)) {
				// Retorna a chave
				return key;
			}
		}
		// Se não encontrar retorna null
		return null;
	}

	
	/** 
	 * Salda todas as dividas do usuário
	 * @param userPayer
	 */
	public void settleUp(User userPayer) {
		// Remove a divida do utilizadorPagante para com todos os utilizadores
		for (Map<String, Integer> map : debtGraph.values()) {
			if (map.containsKey(userPayer.getUsername())) {
				map.remove(userPayer.getUsername());
			}
		}

	}

	
	/** 
	 * Salda a divida do usuário com outro usuário
	 * @param userPayer
	 * @param userReceiver
	 */
	public void settleUp(User userPayer, User userReceiver) {
		// Remove a divida do utilizadorPagante para com o utilizadorDevedor
		debtGraph.get(userReceiver.getUsername()).remove(userPayer.getUsername());
	}

}
