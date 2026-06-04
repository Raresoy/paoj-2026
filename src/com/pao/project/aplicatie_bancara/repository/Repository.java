package com.pao.project.aplicatie_bancara.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfață generică Repository pentru operații CRUD de bază.
 *
 * @param <T>  tipul entității
 * @param <ID> tipul cheii primare
 */
public interface Repository<T, ID> {

    void save(T entity) throws SQLException;

    Optional<T> findById(ID id) throws SQLException;

    List<T> findAll() throws SQLException;

    void update(T entity) throws SQLException;

    void delete(ID id) throws SQLException;
}
