package com.serenityword.mytask;

import com.serenityword.mytask.domain.Task;
import com.serenityword.mytask.domain.User;
import com.serenityword.mytask.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllTasks() {
        User user = new User(); // Créez un objet User
        entityManager.persist(user); // Persistez l'utilisateur en premier

        Task task1 = new Task("Tâche 1");
        Task task2 = new Task("Tâche 2");
        Task task3 = new Task("Tâche 3");

        task1.setTitle("Titre de la tâche 1");
        task2.setTitle("Titre de la tâche 2");
        task3.setTitle("Titre de la tâche 3");

        task1.setUser(user); // Définissez l'utilisateur pour chaque tâche
        task2.setUser(user);
        task3.setUser(user);

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);
        entityManager.flush();

        // Le reste de votre test...
    }
}
