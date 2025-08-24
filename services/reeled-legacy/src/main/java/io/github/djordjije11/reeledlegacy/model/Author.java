package io.github.djordjije11.reeledlegacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "author")
public class Author {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Post> posts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private AuthorType type;

    private String bio;

    private String imageUrl;

    @Column(name = "is_deleted")
    private boolean deleted;

    private ZonedDateTime deletedDate;

    @Version
    private Long version;
}
