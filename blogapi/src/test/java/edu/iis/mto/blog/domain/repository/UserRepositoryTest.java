package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("completely_new_john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }


    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
    	repository.deleteAll();
        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
    	repository.deleteAll();
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = repository.save(user);
        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void findUserShouldThrowException_searchValuesAreNull(){
    	repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(null, null, null);
    }
    
    @Test
    public void findUserShouldNotReturnEmptyList_searchValuesAreEmpty() {
    	List<User> usersAllbyCustomMethod = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "");
    	List<User> usersAll = repository.findAll();
    	MatcherAssert.assertThat("find method with empty arguments, return all users",usersAllbyCustomMethod, Matchers.not(Matchers.is(Matchers.emptyCollectionOf(User.class))));    	
    	//MatcherAssert.assertThat(usersAllbyCustomMethod, Matchers.containsInAnyOrder(usersAll)); say what?
    }
    
    @Test
    public void findUserShouldReturnUser_containingPartOfName() {
    	repository.deleteAll();
    	User persistedUser = repository.save(user);
    	String namePart = persistedUser.getFirstName().substring(0, 3);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(namePart, "", "");
		MatcherAssert.assertThat("find method with filled firs name argument return specific users", users, Matchers.contains(persistedUser));
    }
}
