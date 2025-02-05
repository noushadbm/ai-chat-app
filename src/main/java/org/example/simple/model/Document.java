package org.example.simple.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "embedding", columnDefinition = "vector(384)")
    private double[] embedding;
}
