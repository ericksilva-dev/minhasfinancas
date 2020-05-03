package com.ericksilva.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ericksilva.minhasfinancas.exceptions.ErroAutenticacao;
import com.ericksilva.minhasfinancas.exceptions.RegraNegocioException;
import com.ericksilva.minhasfinancas.model.entity.Usuario;
import com.ericksilva.minhasfinancas.model.repository.UsuarioRepository;
import com.ericksilva.minhasfinancas.service.impl.UsuarioServiceImpl;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("")
public class UsuarioServiceTests {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repositoryMock;
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenário
		Mockito.when(repositoryMock.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("usuario@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(repositoryMock.existsByEmail(Mockito.anyString())).thenReturn(true);
		//ação
		service.validarEmail("usuario@email.com");
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = new Usuario("usuario", email, senha);
		Mockito.when(repositoryMock.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação
		Usuario result = service.autenticar(email, senha);
		
		//veficação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastrado() {
		//cenario
		Mockito.when(repositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado");
	}
	
	@Test
	public void deveLancarUmErroQuandoASenhaNaoEValida() {
		//cenario
		String senha = "senha";
		Usuario usuario = new Usuario("usuario", "usuario@email.com", senha);
		Mockito.when(repositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("usuario@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida");
	}
	
	@Test
	public void deveSalvarUmUsuario() {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
	}
}
