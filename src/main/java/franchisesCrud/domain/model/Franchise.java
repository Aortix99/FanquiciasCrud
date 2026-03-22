package franchisesCrud.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "franchise")
public class Franchise extends AuditableEntity {

    @Column(name = "name", nullable = false, length = 120, unique = true)
    private String name;

    @Column(name = "document", nullable = false, length = 20, unique = true)
    private String document;

    @OneToMany(mappedBy = "franchise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Branch> branches = new ArrayList<>();

    public Franchise() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<Branch> getBranches() {
        return Collections.unmodifiableList(branches);
    }
}
