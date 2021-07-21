package br.com.heldt.testeciss.service;

import br.com.heldt.testeciss.exception.NotFoundException;
import br.com.heldt.testeciss.model.Funcionario;
import br.com.heldt.testeciss.repository.FuncionarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FuncionarioService {

    private FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Optional<Funcionario> findById(Long id) {
        return repository.findById(id);
    }

    public List<Funcionario> findAll() {
        log.info("findAll() funcionários");
        List<Funcionario> funcionarios = parseIterableToList(repository.findAll());
        log.info(String.format("%d funcionários", funcionarios.size()));

        return funcionarios;
    }

    public List<Funcionario> findAllByName(String nome) {
        log.info(String.format("Consultando funcionários por nome (%s)", nome));
        List<Funcionario> funcionarios = parseIterableToList(repository.findAllByNomeContainingIgnoreCase(nome));
        log.info(String.format("%d funcionários", funcionarios.size()));

        return funcionarios;
    }

    private List<Funcionario> parseIterableToList(Iterable<Funcionario> iterable) {
        List<Funcionario> funcionarios = new ArrayList<>();
        iterable.forEach(funcionarios::add);
        return funcionarios;
    }

    public Funcionario save(Funcionario funcionario) {
        log.info(String.format("Salvando o funcionário %s", funcionario.toString()));
        return repository.save(funcionario);
    }

    public void delete(Long id) throws Exception {
        try {
            log.info(String.format("Vai deletar o funcionário %d", id));
            repository.deleteById(id);

        } catch (EmptyResultDataAccessException ex) {
            log.info(String.format("Funcionario pelo ID (%d) não localizado.", id));
            throw new NotFoundException();
        } catch (Exception ex) {
            log.error("Erro ao deletar o funcionário", ex);
            throw ex;
        }
    }

}
