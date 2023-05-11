package project;

public class Logger {



	public void createGroup(ExpensesGroup group) {
		System.out.println("O User: " + group.getCreator().getUsername()
				+ " criou o grupo: " + group.getGroupName());
	}

	public void userAdded(ExpensesGroup group, User userAdded) {
		if (group.usersInGroup().contains(userAdded.getUsername())) {
			System.out.println("O User: " + userAdded.getUsername()
			+ " foi adicionado ao grupo " + group.getGroupName());
		}		
	}

	public void expenseAdded(ExpensesGroup group) {
		Expense lastExpense = group.getExpenses().get(group.getExpenses().size()-1);
		System.out.println("O User: " + lastExpense.getPayer().getUsername() + 
				" pagou " + lastExpense.getDescricao() + " no total de: " + lastExpense.getValor());
		for (String username : group.usersInGroup()) {
			System.out.print("\t | " + username);
			if (lastExpense.getExpenseBalance(username) < 0) {
				System.out.print(" deve " + Math.abs(lastExpense.getExpenseBalance(username)) + System.lineSeparator());
			} else if (lastExpense.getExpenseBalance(username) > 0) {
				System.out.print(" é devido " + lastExpense.getExpenseBalance(username) + 
						" (" + lastExpense.getSplit(username) + ")"+ System.lineSeparator());
			} else {
				System.out.print(" não participou" + System.lineSeparator());
			}
		}
	}

	public void groupSummary(ExpensesGroup group) {
		System.out.println("\n============ Balanço Geral do Grupo ============");
		for (String username : group.usersInGroup()) {
			Integer balance = group.getBalance(UserManager.getCatalog().getUserByUsername(username));
			if (balance < 0) {
				System.out.println(username + " deve " + Math.abs(balance));
			} else if (balance > 0) {
				System.out.println(username + " é devido " + balance);
			} else {
				System.out.println(username + " tem as contas liquidadas globalmente");
			}

		}
		for (String username : group.usersInGroup()) {
			userDebts(group, UserManager.getCatalog().getUserByUsername(username));
		}
		System.out.println("\n===============================================");
	}

	public void userDebts(ExpensesGroup group, User user) {
		System.out.println("\n----> Resumo para " + user.getUsername());
		if (group.getUserDebtors(user).size() == 0 && group.getUserDebts(user).size() == 0) 
			System.out.println("\tContas Liquidadas");
		for (String username : group.usersInGroup()) {
			if (group.getUserDebtors(user).keySet().contains(username)) {
				Integer value = group.getUserDebtors(user).get(username);
				System.out.println("\t" + username + " deve-lhe " + value);
			}
		}
		for (String username : group.usersInGroup()) {
			if (group.getUserDebts(user).keySet().contains(username)) {
				Integer value = group.getUserDebts(user).get(username);
				System.out.println("\tDeve " + value + " a " + username);
			}

		}	
	}

	public void settleUp(User user) {
		System.out.println("\nO User: " + user.getUsername() + " liquidou as suas dividas");
	}

	public void globalBalance(User user) {
		if (user.getCurrentBalance() > 0)
			System.out.println("\n"+user.getUsername()+", é lhe devido " + user.getCurrentBalance()
			+ " no total.");
		else if (user.getCurrentBalance() == 0)
			System.out.println("\n"+user.getUsername() + ": Contas Totalmente Liquidadas");
		else
			System.out.println("\n"+user.getUsername()+" deve " + Math.abs(user.getCurrentBalance())
			+ " no total.");
	}

}
