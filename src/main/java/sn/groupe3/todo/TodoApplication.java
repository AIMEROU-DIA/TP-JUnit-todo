package sn.groupe3.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
		System.out.println("Application TODO démarrée avec succès sur http://localhost:8083");
	}

}
