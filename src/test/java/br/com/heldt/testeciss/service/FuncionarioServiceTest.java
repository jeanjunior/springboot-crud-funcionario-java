package br.com.heldt.testeciss.service;

import br.com.heldt.testeciss.exception.NotFoundException;
import br.com.heldt.testeciss.model.Funcionario;
import br.com.heldt.testeciss.repository.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

class FuncionarioServiceTest {

    private final FuncionarioRepository repository = Mockito.mock(FuncionarioRepository.class);
    private final FuncionarioService service = new FuncionarioService(repository);

    @BeforeEach
    void setUp() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(buildFuncionario(1L)));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());

        Mockito.when(repository.findAll()).thenReturn(
                Arrays.asList(buildFuncionario(1L), buildFuncionario(2L))
        );
        Mockito.when(repository.findAllByNomeContainingIgnoreCase("teste")).thenReturn(
                Collections.singletonList(buildFuncionario(1L))
        );
        Mockito.when(repository.findAllByNomeContainingIgnoreCase("empty")).thenReturn(Collections.emptyList());

        Mockito.when(repository.save(ArgumentMatchers.any(Funcionario.class))).thenAnswer(invocationOnMock -> {
            var model = (Funcionario) invocationOnMock.getArgument(0);
            if (model.getId() == null) {
                model.setId(1L);
            }
            return model;
        });

        Mockito.doNothing().when(repository).deleteById(1L);
        Mockito.doThrow(new EmptyResultDataAccessException("", 2)).when(repository).deleteById(2L);
    }

    private Funcionario buildFuncionario(Long id) {
        return Funcionario.builder()
                .id(id)
                .nome("teste")
                .sobrenome("sobrenome teste")
                .email("email@email")
                .nis(123456789L)
                .build();
    }

    @Test
    void findById() {
        Assertions.assertTrue(service.findById(1L).isPresent(), "Deve retornar uma instancia");
        Assertions.assertFalse(service.findById(2L).isPresent(), "Não deve retornar uma instancia");
    }

    @Test
    void findAll() {
        Assertions.assertFalse(service.findAll().isEmpty(), "Deve retornar uma lista preenchida");
    }

    @Test
    void findAllByName() {
        Assertions.assertFalse(service.findAllByName("teste").isEmpty(), "Deve retornar uma lista preenchida");
        Assertions.assertTrue(service.findAllByName("empty").isEmpty(), "Deve retornar uma lista vazia");
    }

    @Test
    void save() {
        Assertions.assertNotNull(service.save(buildFuncionario(null)).getId(), "Deve retornar a classe com ID preenchido");
        Assertions.assertEquals(service.save(buildFuncionario(2L)).getId(), 2L, "Deve retornar a classe com ID preenchido igual o passado por entrada");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> service.delete(1L), "Deve deletar com sucesso");
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(2L), "Deve retornar exception que não existe o id");
    }
}