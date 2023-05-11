package project;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Classe que representa um utilizador 
 * 
 * @author Labp team
 *
 */
public class User {

	private String username;
	private List<ExpensesGroup> currentGroups;

	/**
	 * Constrói um utilizador
	 * 
	 * @param username
	 */
	public User(String username) {
		this.username = username;
		this.currentGroups = new ArrayList<ExpensesGroup>();
	}

	
	/**
	 * Obtém o username do utilizador
	 * 
	 * @return o username do utilizador
	 */
	public String getUsername() {
		return username;	
	}
	
	/**
	 * Adiciona um grupo ao utilizador
	 * 
	 * @param group
	 * @requires !currentGroups.contains(group)
	 */
	public void addGroup(ExpensesGroup group) {
		currentGroups.add(group) ;
	}
	
	/**
	 * 
	 * Obtém o saldo global do utilizador
	 * 
	 * @return o saldo global do utilizador nos varios
	 * grupos
	 */
	public Integer getCurrentBalance() {
		Integer currentBalance = 0;
		
		for (ExpensesGroup s : currentGroups) {
			currentBalance += s.getBalance(this);
		}
		
		return currentBalance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
}

