package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

import java.math.BigInteger;
import java.security.SecureRandom;
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

	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void findUserShouldThrowException_searchValuesAreNull() {
		repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(null, null, null);
	}

	@Test
	public void findUserShouldNotReturnEmptyList_searchValuesAreEmpty() {
		List<User> usersAllbyCustomMethod = repository
				.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "");
		List<User> usersAll = repository.findAll();
		MatcherAssert.assertThat("find method with empty arguments, return all users", usersAllbyCustomMethod,
				Matchers.not(Matchers.is(Matchers.emptyCollectionOf(User.class))));
		// MatcherAssert.assertThat(usersAllbyCustomMethod,
		// Matchers.containsInAnyOrder(usersAll)); say what?
	}

	@Test
	public void findUserShouldReturnUser_containingPartOfName() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String namePart = persistedUser.getFirstName().substring(0, 3);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				namePart, randomString(), randomString());
		MatcherAssert.assertThat("find method with filled first name argument return specific users", users,
				contains(persistedUser));
	}

	@Test
	public void findUserShouldNotReturnUser_containingPartOfNameInReverseOrder() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String namePart = persistedUser.getFirstName().substring(0, 3);
		namePart = new StringBuilder(namePart).reverse().toString();
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				namePart, randomString(), randomString());
		MatcherAssert.assertThat(
				"find method with filled first name argument in reverse order do not return specific users", users,
				not(contains(persistedUser)));
	}

	@Test
	public void findUserShouldReturnUser_byFullName() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String name = persistedUser.getFirstName();
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(name,
				randomString(), randomString());
		MatcherAssert.assertThat("find method with filled first name argument return specific users", users,
				contains(persistedUser));
	}

	@Test
	public void findUserShouldNotReturnUser_byFullNameInReverseOrder() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String name = persistedUser.getFirstName();
		name = new StringBuilder(name).reverse().toString();
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(name,
				randomString(), randomString());
		MatcherAssert.assertThat(
				"find method with filled first name argument in reverse order do not return specific users", users,
				not(contains(persistedUser)));
	}

	@Test
	public void findUserShouldReturnUser_byEmail() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String email = persistedUser.getEmail();
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(randomString(),
				randomString(), email);
		MatcherAssert.assertThat("find method with filled first name argument return specific users", users,
				contains(persistedUser));
	}

	@Test
	public void findUserShouldNotReturnUser_byWrongEmail() {
		repository.deleteAll();
		User persistedUser = repository.save(user);
		String email = persistedUser.getEmail();
		email = new StringBuilder(email).reverse().toString();
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(randomString(),
				randomString(), email);
		MatcherAssert.assertThat(
				"find method with filled first name argument in reverse order do not return specific users", users,
				not(contains(persistedUser)));
	}

	private String randomString() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}
}
