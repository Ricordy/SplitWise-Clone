package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import project.ExpensesGroup;
import project.User;

public class ExpensesGroupTests {


	@Test
	public void test_constructor() {
		User creator = new User("Carlos");
		ExpensesGroup group1 = new ExpensesGroup("Test1", creator);
		assertEquals("Carlos", group1.getCreator().getUsername());
		assertEquals(0, (int) group1.getBalance(creator));
	}

	@Test
	public void test_add_user() {
		User creator = new User("Carlos");
		ExpensesGroup group1 = new ExpensesGroup("Test1", creator);
		User user1 = new User("Pedro");
		group1.addUser(user1);
		assertEquals(0, (int) group1.getBalance(user1));
	}

	@Test
	public void test_add_expense_group2() {
		User userCarlos = new User("Carlos");
		ExpensesGroup group1 = new ExpensesGroup("Group2", userCarlos);
		User userPedro = new User("Pedro");
		group1.addUser(userPedro);

		group1.addExpense("Presente", userPedro, 2500);
		assertEquals(1250, (int) group1.getBalance(userPedro));
		assertTrue(group1.getUserDebtors(userPedro).containsKey(userCarlos.getUsername()));
	}

	@Test
	public void test_add_expense_group3() {
		User userMarta = new User("Marta");
		ExpensesGroup group3 = new ExpensesGroup("Group3", userMarta);
		User userJoana = new User("Joana");
		group3.addUser(userJoana);
		User userRita = new User("Rita");
		group3.addUser(userRita);

		group3.addExpense("Jantar", userRita, 9900);
		group3.addExpense("Sobremesa", userRita, 1800);
		group3.addExpense("Cinema", userJoana, 2500);

		assertTrue(group3.getUserDebts(userRita).isEmpty());
		assertTrue(group3.getUserDebtors(userRita).containsKey(userMarta.getUsername()));
		assertEquals(3067, (int) group3.getUserDebtors(userRita).get(userJoana.getUsername()));
		assertEquals(3900, (int) group3.getUserDebtors(userRita).get(userMarta.getUsername()));
		assertEquals(834, (int) group3.getUserDebtors(userJoana).get(userMarta.getUsername()));
	}

	@Test
	public void test_settleUp_group3() {
		User userMarta = new User("Marta");
		ExpensesGroup group3 = new ExpensesGroup("Group3", userMarta);
		User userJoana = new User("Joana");
		group3.addUser(userJoana);
		User userRita = new User("Rita");
		group3.addUser(userRita);

		group3.addExpense("Jantar", userRita, 9900);
		group3.addExpense("Sobremesa", userRita, 1800);

		group3.settleUp(userMarta);
		assertFalse(group3.getUserDebtors(userRita).containsKey(userMarta.getUsername()));
		assertEquals(0, (int) group3.getBalance(userMarta));
		assertEquals(3900, (int) group3.getBalance(userRita));

		group3.addExpense("Cinema", userJoana, 2500);
		assertFalse(group3.getUserDebts(userJoana).containsKey(userMarta.getUsername()));

		group3.settleUp(userMarta);
		group3.settleUp(userJoana);
		assertEquals(0, (int) group3.getBalance(userMarta));
		assertEquals(0, (int) group3.getBalance(userJoana));
	}

	@Test
	public void test_all_paid() {
		User userMarta = new User("Marta");
		ExpensesGroup group3 = new ExpensesGroup("Group3", userMarta);
		User userJoana = new User("Joana");
		group3.addUser(userJoana);
		User userRita = new User("Rita");
		group3.addUser(userRita);

		group3.addExpense("Jantar", userRita, 8100);
		group3.addExpense("Sobremesa", userMarta, 4200);		
		group3.addExpense("Cinema", userJoana, 900);

		assertEquals(2400, (int) group3.getUserDebtors(userRita).get(userJoana.getUsername()));
		assertTrue(group3.getUserDebtors(userJoana).isEmpty());

		assertEquals(1100, (int) group3.getUserDebtors(userMarta).get(userJoana.getUsername()));
	}

	@Test
	public void test_many_groups() {
		User userEurico = new User("Eurico");
		User userMaria = new User("Maria");
		User userMarta= new User("Marta");
		User userPedro = new User("Pedro");

		ExpensesGroup group1 = new ExpensesGroup("Group1", userEurico);
		group1.addUser(userMarta);
		group1.addUser(userPedro);

		ExpensesGroup group2 = new ExpensesGroup("Group2", userEurico);
		group2.addUser(userMaria);

		ExpensesGroup group3 = new ExpensesGroup("Group3", userPedro);
		group3.addUser(userMarta);
		group3.addUser(userMaria);
		group3.addUser(userEurico);


		group3.addExpense("Presente3", userEurico, 3550);
		assertEquals(2663, (int) userEurico.getCurrentBalance());

		group2.addExpense("Presente2", userEurico, 155600);
		assertEquals(80463, (int) userEurico.getCurrentBalance());

		group1.addExpense("Presente1", userEurico, 34500);
		assertEquals(103463, (int) userEurico.getCurrentBalance());

	}



}
