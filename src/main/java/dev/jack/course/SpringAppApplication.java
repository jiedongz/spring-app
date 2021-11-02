package dev.jack.course;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import dev.jack.course.dao.*;
import dev.jack.course.model.*;

@SpringBootApplication
public class SpringAppApplication {
	private static DAO<Course> dao;
	
	public SpringAppApplication(DAO<Course> dao) {
		this.dao = dao;
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringAppApplication.class, args);
		System.out.println("\n Create Course -------------------------\n");
		Course springVue = new Course(1, "Spring boot + Vue", "New Course", "https://www.danvega.dev/course");
		dao.create(springVue);
		
		System.out.println("\n One Course -------------------------\n");
		Optional<Course> firstOne = dao.get(10);
		System.out.println(firstOne.get());
		
		springVue.setDescription("Learn to build Vue apps that talk to Spring boot");
		dao.update(springVue, 6);
		
		dao.delete(4);
		
		System.out.println("\n All Course -------------------------\n");
		List<Course> courses = dao.list();
		courses.forEach(System.out::println);
	}

}
