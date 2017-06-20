package edu.iis.mto.blog.domain;

import java.util.Optional;
import java.util.Random;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

	@MockBean
	UserRepository userRepository;

	@MockBean
	LikePostRepository likePostRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	@MockBean
	BlogPostRepository blogPostRepository;

	private final User user = new User();
	private final User postOwner = new User();
	private final BlogPost post = new BlogPost();
	private final Long blogpostID = (new Random()).nextLong();
	private final Long postOwnerID = (new Random()).nextLong();
	private final Long userID = (new Random()).nextLong();

	@Before
	public void setUpTest() {
		user.setAccountStatus(AccountStatus.NEW);
		user.setFirstName("Test User");
		user.setLastName("Test User");
		user.setEmail("Test@test.com");
		user.setId(userID);

		postOwner.setAccountStatus(AccountStatus.CONFIRMED);
		postOwner.setFirstName("Test User");
		postOwner.setLastName("Test User");
		postOwner.setEmail("Test@test.com");
		postOwner.setId(postOwnerID);

		post.setUser(postOwner);
		post.setEntry("Test");
		post.setId(blogpostID);
	}

	@Test
	public void creatingNewUserShouldSetAccountStatusToNEW() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
		Mockito.verify(userRepository).save(userParam.capture());
		User user = userParam.getValue();
		Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
	}

	@Test(expected = DomainError.class)
	public void userWithoutCONFIRMEDStatustryingToLikePost_throwException() {
		Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
		Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
		Mockito.when(likePostRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
		blogService.addLikeToPost(user.getId(), post.getId());
	}
	
	
}
