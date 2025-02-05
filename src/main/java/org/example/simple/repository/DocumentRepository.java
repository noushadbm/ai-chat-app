package org.example.simple.repository;

import org.example.simple.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query(value = "SELECT * FROM documents ORDER BY embedding <=> cast(:embedding as vector) LIMIT :k", nativeQuery = true)
    List<Document> findNearestNeighbors(@Param("embedding") double[] embedding, @Param("k") int k);
}
