package com.ericksilva.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ericksilva.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
