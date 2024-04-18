package com.aston.shop.users.repository;

import com.aston.shop.users.entity.User;
import com.aston.shop.users.utils.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTests {
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("Test save user functionality")
	void givenUserToObject_whenSave_thenUserIsCreated() {
		//given
		User userPersisted = DataUtils.getMichaelPersisted();
		// when
		User save = userRepository.save(userPersisted);
		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isNotNull();
	}

	@Test
	@DisplayName("Test update user functionality")
	void givenUserToUpdate_whenSave_thenEmailIsChanged() {
		// given
		User userPersisted = DataUtils.getMichaelPersisted();
		String email = "upd@newEmail.ru";
		userRepository.saveAndFlush(userPersisted);
		//when
		User user = userRepository.findById(userPersisted.getId()).orElse(null);
		System.out.println(user);
		user.setEmail(email);
		User saveAndFlush = userRepository.saveAndFlush(user);
		//then
		assertThat(saveAndFlush).isNotNull();
		assertThat(saveAndFlush.getEmail()).isEqualTo(email);
	}


	@Test
	@DisplayName("Test get user id functionality")
	void givenUserCreated_whenGetById_thenUserIsReturned() {
		// given
		User user = DataUtils.getMichaelPersisted();
		Long id = user.getId();
		userRepository.save(user);
		//when
		User userById = userRepository.findById(id).orElse(null);
		//then
		assertThat(userById.getId()).isEqualTo(id);
	}

	@Test
	@DisplayName("Test get user not found functionality")
	void givenUserIsNotCreated_whenGetById_thenOptionalEmpty() {
		//given

		//when
		User user = userRepository.findById(1L).orElse(null);
		//then
		assertThat(user).isNull();
	}

	@Test
	@DisplayName("Test Get all users functionality")
	void givenUsersIsSaved_whenFindAll_thenListUsersReturned() {
		//given
		User johnTransient = DataUtils.getJohnTransient();
		User michaelTransient = DataUtils.getMichaelTransient();
		User annaTransient = DataUtils.getAnnaTransient();

		userRepository.saveAll(List.of(johnTransient, michaelTransient, annaTransient));
		//when
		List<User> users = userRepository.findAll();
		//then
		assertThat(CollectionUtils.isEmpty(users)).isFalse();
	}

	@Test
	@DisplayName("Test exists by username user functionality")
	void givenUserIsSaved_whenExistsByUsername_thenBooleanReturned() {
		//given
		User michaelTransient = DataUtils.getMichaelTransient();
		userRepository.save(michaelTransient);
		//when
		boolean existsedByUsername = userRepository.existsByUsername(michaelTransient.getUsername());
		//then
		assertThat(existsedByUsername).isTrue();
	}

	@Test
	@DisplayName("Test exists by email user functionality")
	void givenUserIsSaved_whenExistsByEmail_thenBooleanReturned() {
		//given
		User johnTransient = DataUtils.getJohnTransient();
		userRepository.save(johnTransient);
		//when
		boolean existsedByEmail = userRepository.existsByEmail(johnTransient.getEmail());
		//then
		assertThat(existsedByEmail).isTrue();
	}

	@Test
	@DisplayName("Test delete user by id functionality")
	void givenUserIsSaved_whenDeleteById_thenUserIsRemovedFromDB() {
		//given
		User annaPersisted = DataUtils.getAnnaPersisted();
		userRepository.save(annaPersisted);
		//when
		userRepository.deleteById(annaPersisted.getId());
		//then
		User user = userRepository.findById(annaPersisted.getId()).orElse(null);
		assertThat(user).isNull();
	}
}
