package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import project.Expense;
import project.User;

public class ExpenseTests {
	
	
	@Test
    public void test_constructor() {
		User payer = new User("Carlos");
		List<String> people = List.of("Maria", "Carlos", "Joao");
		Expense expense = new Expense("Test1", payer, 2700, people);
		assertEquals("Carlos", expense.getPayer().getUsername());
		assertEquals(2700, (int) expense.getValor());
	}

	
	@Test
    public void test_split_expense_equal() {
		User payer = new User("Joao");
		List<String> people = List.of("Maria", "Carlos", "Joao");
		Expense expense = new Expense("Test2", payer, 6000, people);
		assertEquals(2000, (int) expense.getSplit("Joao"));
		assertEquals(4000, (int) expense.getExpenseBalance("Joao"));
		assertEquals(-2000, (int) expense.getExpenseBalance("Maria"));
	}
	
	@Test
    public void test_split_expense_different() {
		User payer = new User("Maria");
		List<String> people = List.of("Maria", "Carlos", "Joao");
		List<Double> howToSplit = List.of(0.5, 0.25, 0.25);
		Expense expense = new Expense("Test3", payer, 5000, people, howToSplit);
		assertEquals(2500, (int) expense.getExpenseBalance("Maria"));
		assertEquals(-1250, (int) expense.getExpenseBalance("Joao"));
	}
	
	@Test
    public void test_split_expense_not_involved() {
		User payer = new User("Carlos");
		List<String> people = List.of("Maria", "Joao", "Carlos");
		List<Double> howToSplit = List.of(0.25, 0.75, 0.0);
		Expense expense = new Expense("Test4", payer, 10000, people, howToSplit);
		assertEquals(10000, (int) expense.getExpenseBalance("Carlos"));
		assertEquals(-2500, (int) expense.getExpenseBalance("Maria"));
		assertEquals(-7500, (int) expense.getExpenseBalance("Joao"));
	}
	
	
	@Test
    public void test_two_people_equal() {
		User payer = new User("Maria");
		List<String> people = List.of("Joao", "Maria");
		Expense expense = new Expense("Test5", payer, 2000, people);
		assertEquals(1000, (int) expense.getExpenseBalance("Maria"));
		assertEquals(-1000, (int) expense.getExpenseBalance("Joao"));
	}
	
	@Test
    public void test_two_people_different() {
		User payer = new User("Maria");
		List<String> people = List.of("Joao", "Maria");
		List<Double> howToSplit = List.of(1.0, 0.0);
		Expense expense = new Expense("Test5", payer, 2000, people, howToSplit);
		assertEquals(2000, (int) expense.getExpenseBalance("Maria"));
		assertEquals(-2000, (int) expense.getExpenseBalance("Joao"));
	}
	
	
	
	
	
}
