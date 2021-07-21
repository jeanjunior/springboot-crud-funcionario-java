package br.com.heldt.testeciss.repository;

import br.com.heldt.testeciss.model.Funcionario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends CrudRepository<Funcionario, Long> {

    Iterable<Funcionario> findAllByNomeContainingIgnoreCase(String nome);

}
