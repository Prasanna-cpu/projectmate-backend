package com.spring.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issues")
public class Issue extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String priority;
    private LocalDate dueDate;

    @ElementCollection
    @CollectionTable(name = "issue_tags", joinColumns = @JoinColumn(name = "issue_id"))
    @Column(name = "tag") // Convert List<String> to String with comma-separated values.
    private List<String> tags=new ArrayList<String>();

    @ManyToOne
    private User assignee;

    @ManyToOne
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "issue",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments=new ArrayList<>();
}
