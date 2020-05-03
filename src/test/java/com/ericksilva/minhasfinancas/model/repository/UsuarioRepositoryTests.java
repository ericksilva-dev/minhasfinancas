package com.ericksilva.minhasfinancas.model.repository;import java.util.Optional;

import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ericksilva.minhasfinancas.model.entity.Usuario;


@RunWith(SpringRunner.class)
@ActiveProfiles("tests")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTests {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = new Usuario("erick silva", "teste@email.com");
		entityManager.persist(usuario);
		//ação / execução
		boolean result = repository.existsByEmail("teste@email.com");
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveVerificarASeNaoExisteEmailCadastrado() {
		//ação / execução
		boolean result = repository.existsByEmail("teste@email.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
		Usuario usuarioSalvo = entityManager.persist(usuario);
		
		//validação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		// verificacao
		Optional<Usuario> result = repository.findByEmail("teste@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornaVazioAoBuscarUsuarioPorEmail() {
		// verificacao
		Optional<Usuario> result = repository.findByEmail("teste@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		return new Usuario("usuario", "teste@email.com", "senha");
	}
}
