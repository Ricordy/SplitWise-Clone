package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * 
 * Classe que representa um User Manager de utilizadores no sistema
 * 
 * @author Labp team
 *
 */
public class UserManager {
	
	private static UserManager instance;
	
	private Map<String, User> usersCatalog;
	

	private UserManager() {
		usersCatalog = new HashMap<String,User>();
	}
	
	/**
	 * 
	 * Obtem a instancia de userManager. 
	 * Segue o padrão Singleton
	 * 
	 * @return a instancia unica de UserManager no sistema
	 */
	public static UserManager getCatalog() {
		if (instance == null)
			instance = new UserManager();
		
		return instance;
	}
	
	
	/**
	 * 
	 * Obtem o User com o username username
	 * 
	 * @param username
	 * @return o User correspondente ao username
	 */
	public User getUserByUsername(String username) {
		return usersCatalog.get(username);
	}
	
	/*
	 * Adiciona o User user ao usersCatalog
	 * 
	 * @requires !usersCatalog.contains(user.getUsername())
	 */
	public User addUser(User user) {
		usersCatalog.put(user.getUsername(), user);
		return user;
	}
	
	
	/**
	 * Carrega uma série de novos utilizadores em usersCatalog
	 * 
	 * @param fileName
	 * @throws FileNotFoundException 
	 */
	public void loadUsers(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String username = s.nextLine();
			usersCatalog.put(username, new User(username));
		}
		s.close();
	}
	
	
	

}
