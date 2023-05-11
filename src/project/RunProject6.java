package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunProject6 {

	private static User JOANA;
	private static User GABRIEL;
	private static User MARIA;
	
	
	private static Logger logger = new Logger();

	public static void main(String[] args) throws FileNotFoundException {
		createUsers();

		groupOne();
		
		logger.globalBalance(GABRIEL);
		
		finalTest();
	}


	private static void groupOne() {
		User userCreator = JOANA;
		ExpensesGroup group1 = new ExpensesGroup("Grupo1", userCreator);
		logger.createGroup(group1);
		
		group1.addUser(GABRIEL);
		logger.userAdded(group1, GABRIEL);
		
		group1.addUser(MARIA);
		logger.userAdded(group1, MARIA);
		
		group1.addExpense("Bilhetes Cinema", GABRIEL, 3790);
		logger.expenseAdded(group1);
		
		logger.groupSummary(group1);
		
		group1.addExpense("Jantar", JOANA, 6900);
		logger.expenseAdded(group1);
		
		logger.groupSummary(group1);
		
		group1.addExpense("Pipocas", MARIA, 1000);
		logger.expenseAdded(group1);
		
		logger.groupSummary(group1);
		
		group1.settleUp(GABRIEL);
		logger.settleUp(GABRIEL);
		
		logger.userDebts(group1, GABRIEL);
		
	}

	private static void createUsers() {
		UserManager manager = UserManager.getCatalog();
		JOANA = manager.addUser(new User("Joana"));
		GABRIEL = manager.addUser(new User("Gabriel"));
		MARIA = manager.addUser(new User("Maria"));
	}

	private static void finalTest() throws FileNotFoundException {
		UserManager manager = UserManager.getCatalog();
		manager.loadUsers("IO/inputUsers.txt");
		ExpensesGroup group = null;
		Scanner inputFile = new Scanner(new File("IO/input.txt"));
		while (inputFile.hasNextLine()) {
			String line = inputFile.nextLine();
			switch (line.split(" ")[0]) {
			case "G:":
				User creator = manager.getUserByUsername(line.split(" ")[2]);
				group = new ExpensesGroup(line.split(" ")[1], creator);
				logger.createGroup(group);
				break;
			case "A:":
				User userAdded = manager.getUserByUsername(line.split(" ")[1]);
				group.addUser(userAdded);
				logger.userAdded(group, userAdded);
				break;
			case "E:":
				User payer = manager.getUserByUsername(line.split(" ")[2]);
				Integer value = Integer.parseInt(line.split(" ")[3]);
				if (line.split(" ").length < 5) {
					group.addExpense(line.split(" ")[1], payer, value);
				} else {
					List<Double> howToSplit = new ArrayList<>();
					for (String s : line.split(" ")[4].split(",")) {
						howToSplit.add(Double.parseDouble(s));
					}
					group.addExpense(line.split(" ")[1], payer, value, howToSplit);
				}
				logger.expenseAdded(group);
				break;
			case "S:":
				User payerS = manager.getUserByUsername(line.split(" ")[1]);
				if (line.split(" ").length < 3) {
					group.settleUp(payerS);
				} else {
					User receiver = manager.getUserByUsername(line.split(" ")[1]);
					group.settleUp(payerS, receiver);
				}
				logger.settleUp(payerS);
				break;
			case "B:":
				logger.globalBalance(manager.getUserByUsername(line.split(" ")[1]));
				break;
			case "BG":
				logger.groupSummary(group);
			default:
				break;
			}
			
		}
		
	}

}
